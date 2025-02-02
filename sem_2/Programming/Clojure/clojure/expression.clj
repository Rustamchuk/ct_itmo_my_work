(defn constant [a] (fn[& _] a))
(defn variable [v] #(get % v))
(defn operation [f] (fn [& args] (fn [vars] (apply f (map #(% vars) args)))))
(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))

;; Mods
(def exp (operation #(Math/exp %)))
(def ln (operation #(Math/log %)))

(defn division [& args] (reduce #(/ (double %1) (double %2)) args))
(def divide (operation division))
(def negate (operation -))
(def operMap {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'ln ln, 'exp exp})
(defn parse [expr] (cond
  (seq? expr) (apply (get operMap (first expr)) (map #(parse %) (rest expr)))
  (number? expr) (constant expr)
  (symbol? expr) (variable (str expr))))
(defn parseFunction [expr] (parse (read-string expr)))

;; TestClojure.cmd cljtest.functional.FunctionalTest easy base

(defn evaluate [f vars] ((f :evaluate) vars))
(defn toString [f] (f :toString))

(defn Expression [string eval] 
{
    :toString string
    :evaluate eval
})

(defn Constant [a] (Expression (str a) (fn [& _] a)))
(defn Variable [v] (Expression v #(get % v)))

(defn join [args] (if (= (count args) 1) (first args) (reduce #(str %1 " " %2) args)))

(defn Operation [f st] (fn [& args] (Expression
    (str "(" st " " (join (map #(toString %) args)) ")")
    (fn [vars] (apply f (map #(evaluate % vars) args)))
)))

(def Add (Operation + "+"))
(def Subtract (Operation - "-"))
(def Multiply (Operation * "*"))
(def Divide (Operation division "/"))
(def Negate (Operation - "negate"))

;; Mods
(def Exp (Operation #(Math/exp %) "exp"))
(def Ln (Operation #(Math/log (Math/abs %)) "ln"))
(def Pow (Operation #(Math/pow %1 %2) "pow"))
(def Log (Operation #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1))) "log"))
(def Atan (Operation #(Math/atan %) "atan"))
(def Atan2 (Operation #(Math/atan2 %1 %2) "atan2"))
(def Sin (Operation #(Math/sin %) "sin"))
(def Cos (Operation #(Math/cos %) "cos"))

(def objMap {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'sin Sin, 'cos Cos, 'atan Atan, 'atan2 Atan2, 'exp Exp, 'ln Ln, 'pow Pow, 'log Log})

(defn parseObj [expr] (cond
  (seq? expr) (apply (get objMap (first expr)) (map #(parseObj %) (rest expr)))
  (number? expr) (Constant expr)
  (symbol? expr) (Variable (str expr))))
(defn parseObject [expr] (parseObj (read-string expr)))

(def expr
  (Subtract
    (Multiply
      (Constant 2)
      (Variable "x"))
    (Constant 3)))
(def par (parseObject "(atan2 841 540)"))
;; (println (evaluate expr {"x" 2}))
(println (evaluate par {"x" 2}))

;; TestClojure.cmd cljtest.object.ObjectTest easy base

; Load the provided code
(load-file "parser.clj")

;; Parser for numbers
(def number-parser
  (+map (fn [s] (Constant (Integer/parseInt s)))
        (+str (+plus (+char (range \0 \9))))))

;; Parser for variables
(def variable-parser
  (+map (fn [s] (Variable s))
        (+str (+plus (+char (range \a \z))))))

;; Parser for term
(def term-parser
  (+or number-parser variable-parser))

;; Parser for factor
(def factor-parser
  (+or
    (+seqn 1 (+char #{\(}) term-parser (+char #{\)}))
    term-parser))

;; Parser for operation
(def operation-parser
  (+seqf
    (fn [op args]
      (let [op-fn (get objMap (keyword op))]
        (apply op-fn args)))
    (+str (+plus (+char #{\+ \- \* \/})))
    (+star factor-parser)))

;; Parser for postfix-expr
(def postfix-expr-parser
  (+or
    (+seqn 1 (+char #{\(}) operation-parser (+char #{\)}))
    factor-parser))

;; Function to parse postfix expressions
(defn parseObjectPostfix [expr]
  (+parser postfix-expr-parser expr))

;; Function to convert an expression object to its postfix string representation
(defn toStringPostfix [expr]
  (expr :toString))