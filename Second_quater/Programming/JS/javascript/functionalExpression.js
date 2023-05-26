"use strict";

const cnst = a => _ => a;
const variable = sym => (x, y, z) => ({ x, y, z }[sym]);
const one = cnst(1);
const two = cnst(2);

const universal = f => (...args) => (x, y, z) => f(...args.map(a => a(x, y, z)));

const add = universal((...args) => args.reduce((a, b) => (a + b)));
const subtract = universal((...args) => args.reduce((a, b) => (a - b)));
const multiply = universal((...args) => args.reduce((a, b) => (a * b)));
const divide = universal((...args) => args.reduce((a, b) => (a / b)));

const negate = universal(a => -a);
const sin = universal(a => Math.sin(a));
const cos = universal(a => Math.cos(a));

let exp = add(subtract(
    multiply(variable("x"), variable("x")), 
    multiply(cnst(2), variable("x"))), cnst(1));
for (let i = 0; i <= 10; i++) {
    println(exp(i, 0, 0));
}