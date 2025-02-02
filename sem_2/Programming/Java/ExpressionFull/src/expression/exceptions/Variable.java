package expression.exceptions;

import expression.MyExpression;

import java.util.Objects;

public class Variable implements MyExpression {
    String a;

    public Variable(String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return a.toString();
    }

    @Override
    public int evaluate(int value) {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (a.toString().equals("x"))
            return x;
        else if (a.toString().equals("y"))
            return y;
        else
            return z;
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof Variable var)
            // not equals of toString()
            return toString().equals(var.toString());
        else
            return false;
    }
}