(defn oper [f] 
    (fn [& vecs] (apply mapv f vecs)))
(def v+ (oper +))
(def v- (oper -))
(def v* (oper *))
(def vd (oper /))

(defn scalar [a b] (apply + (v* a b)))

(defn cross [a b i j] (- (* (nth a i) (nth b j)) (* (nth a j) (nth b i))))
(defn vect [a b] (vector (cross a b 1 2) (cross a b 2 0) (cross a b 0 1)))

(defn v*s [v s] (mapv #(* % s) v))

(def m+ (oper v+))
(def m- (oper v-))
(def m* (oper v*))
(def md (oper vd))

(defn m*s [m s] (mapv #(v*s % s) m))

(defn m*v [m v] (mapv #(scalar % v) m))

(defn transpose [m] (apply mapv vector m))
(defn m*m [a b] (mapv #(m*v (transpose b) %) a))

(defn numbers [func a b]
  (if (or (vector? a) (vector? b))
    (mapv #(numbers func %1 %2) a b)
    (func a b)))

(defn s+ [a b] (numbers + a b))
(defn s- [a b] (numbers - a b))
(defn s* [a b] (numbers * a b))
(defn sd [a b] (numbers / a b))

(def v1 [1 2 3])
(def v2 [4 5 6])
(def m1 [[1 2 3] [4 5 6]])
(def m2 [[7 8 9] [10 11 12]])
(println "v+:" (v+ v1 v2)) ; [5 7 9]
(println "v-:" (v- v1 v2)) ; [-3 -3 -3]
(println "v*:" (v* v1 v2)) ; [4 10 18]
(println "vd:" (vd v1 v2)) ; [1/4 2/5 1/2]
(println "scalar:" (scalar v1 v2)) ; 32
(println "vect:" (vect v1 v2)) ; [-3 6 -3]
(println "v*s:" (v*s v1 2)) ; [2 4 6]
(println "m+:" (m+ m1 m2)) ; [[8 10 12] [14 16 18]]
(println "m-:" (m- m1 m2)) ; [[-6 -6 -6] [-6 -6 -6]]
(println "m*:" (m* m1 m2)) ; [[7 16 27] [40 55 72]]
(println "md:" (md m1 m2)) ; [[1/7 1/4 1/3] [2/5 5/11 1/2]]
(println "m*s:" (m*s m1 2)) ; [[2 4 6] [8 10 12]]
(println "m*v:" (m*v m1 v1)) ; [14 32]
(println "m*m:" (m*m m1 m2)) ; [[27 30 33] [78 87 96]]
(println "sss:" (s+ [5] [3])) ; [[27 30 33] [78 87 96]]
;; (println "m*m:" (map vector [5 3])) ; [[27 30 33] [78 87 96]]
(println "transpose:" (transpose m1)) ; [[1 4] [2 5] [3 6]]