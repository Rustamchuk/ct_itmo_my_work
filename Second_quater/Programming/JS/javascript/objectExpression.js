"use strict";

function Expression() {}

Expression.prototype.evaluate = function() {};
Expression.prototype.toString = function() {};
Expression.prototype.prefix = function() {};

function Const(a) {
    this.a = a;
}

Const.prototype = Object.create(Expression.prototype);
Const.prototype.constructor = Const;
Const.prototype.evaluate = function() { return this.a; };
Const.prototype.toString = function() { return this.a.toString(); };
Const.prototype.prefix = function() { return this.a.toString(); };

function Variable(sym) {
    this.sym = sym;
}

const vars = { "x": 0, "y": 1, "z": 2 };

Variable.prototype = Object.create(Expression.prototype);
Variable.prototype.constructor = Variable;
Variable.prototype.evaluate = function(...args) { return args[vars[this.sym]]; };
Variable.prototype.toString = function() { return this.sym; };
Variable.prototype.prefix = function() { return this.sym; };

const keyWords = {}

function MakeOperation(f, s, n) {
    const operation = function (...args) {
        this.args = args;
    };
    operation.prototype = Object.create(Expression.prototype);
    operation.prototype.f = f;
    operation.prototype.s = s;
    operation.prototype.constructor = operation;
    operation.prototype.evaluate = function(x, y, z) {
        return this.f(...this.args.map(a => a.evaluate(x, y, z)));
    };
    operation.prototype.toString = function() {
        return this.args.join(" ") + " " + this.s;
    };
    operation.prototype.prefix = function() {
        return "(" + this.s + " " + this.args.map(a => a.prefix()).join(" ") + ")"
    };
    keyWords[s] = [operation, n];
    return keyWords[s][0];
}

const Add = MakeOperation((a, b) => (a + b), "+", 2);
const Subtract = MakeOperation((a, b) => (a - b), "-", 2);
const Multiply = MakeOperation((a, b) => (a * b), "*", 2);
const Divide = MakeOperation((a, b) => (a / b), "/", 2);
const Negate = MakeOperation(a => -a, "negate", 1);
const Exp = MakeOperation(a => Math.exp(a), "exp", 1);
const Ln = MakeOperation(a => Math.log(a), "ln", 1);
const Sum = MakeOperation((...args) => args.length !== 0 ? args.reduce((a, b) => (a + b)) : 0, "sum", Infinity);
const Avg = MakeOperation((...args) => args.length !== 0 ? args.reduce((a, b) => (a + b)) / args.length : 0, "avg", Infinity);

function isWhitespace(char) {
    return char.trim() === "";
}

const parse = function(expression) {
    let ind = 0;
    let stack = [];
    expression += " ";
    for (let i = 0; i < expression.length; i++) {
        if (isWhitespace(expression[i])) {
            const word = expression.substring(ind, i);
            ind = i + 1;
            if (word === "") {
                continue;
            }
            if (word in keyWords) {
                stack.push(new keyWords[word][0](...stack.splice(-keyWords[word][1], keyWords[word][1])));
            } else {
                if (word in vars) {
                    stack.push(new Variable(word));
                } else {
                    stack.push(new Const(parseInt(word)));
                }
            }
        }
    }
    return stack.pop();
}

function ParseError(message) {
    Error.call(this, message);
    this.message = message
}

const parser = {
    parse(expr) {
        StringSourse.call(this, expr);
        let end = this.parseOperations();
        if (end == null) {
            end = this.checkMember(this.word);
        }
        this.skip();
        if (this.hasNext()) {
            throw new ParseError("Extra symbols at the end!");
        }
        return end;
    },

    parseOperations() {
        this.skip();
        if (this.checkBrackets()) {
            return this.callBrackets();
        }
        const oper = this.buildWord();
        if (oper in keyWords) {
            if (keyWords[oper][1] == Infinity) {
                const args = [];
                this.skip();
                while (this.hasNext() && !this.isNext(")")) {
                    args.push(this.parseMember());
                    this.skip();
                }
                return new keyWords[oper][0](...args);
            }
            return new keyWords[oper][0](...Array.from(Array(keyWords[oper][1]), () => this.parseMember()));
        }
        return null;
    },

    parseMember() {
        this.skip();
        if (this.checkBrackets()) {
            return this.callBrackets();
        }
        return this.checkMember(this.buildWord());
    },

    buildWord() {
        this.word = "";
        while(this.hasNext() && !this.isNext(...this.skipElems)) {
            this.word += this.next();
        }
        if (this.word === "") {
            throw new ParseError("No word");
        }
        return this.word;
    },

    checkMember(member) {
        if (member in vars) {
            return new Variable(member);
        } else if (!isNaN(member)) {
            return new Const(parseInt(member));
        } else {
            throw new ParseError("No member");
        }
    },

    checkBrackets() {
        if (this.isNext(")")) {
            throw new ParseError("No open Bracket!");
        }
        return this.isNext("(");
    },

    callBrackets() {
        this.next();
        const res = this.parseOperations();
        if (res == null) {
            throw new ParseError("No operation in bracket");
        }
        this.skip();
        if (this.isNext(")")) {
            this.next();
        } else {
            throw new ParseError("No Close Bracket!");
        }
        return res;
    },

    skip() {
        while (this.hasNext() && this.isNext(" ")) {
            this.next();
        }
    }
}

function StringSourse(expr) {
    this.skipElems = ["(", ")", " "];
    this.expression = expr;
    this.pos = 0;
    
    this.next = () => this.expression[this.pos++];
    this.isNext = (...x) => x.includes(this.expression[this.pos]);
    this.hasNext = () => this.pos < this.expression.length;
    this.back = () => this.pos--;
}

const parsePrefix = (expression) => parser.parse(expression);

// let a = parse("x x * 2 x * - 1 +");
// println(a.evaluate(5, 0, 0));
// println(a.toString());

// let exp = new Add(new Subtract(
//     new Multiply(new Variable("x"), new Variable("x")), 
//     new Multiply(new Const(2), new Variable("x"))), new Const(1));
// for (let i = 0; i <= 10; i++) {
//     println(exp.evaluate(i, 0, 0));
//     println(exp.toString());
// }
let a = parsePrefix("1");
println(a.evaluate(0, 0, 0));