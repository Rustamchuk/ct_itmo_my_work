package expression.exceptions;

import expression.MyExpression;

import java.util.Objects;

public class Const implements MyExpression {

    int a;
    public Const(int a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return String.valueOf(a);
    }

    @Override
    public int evaluate(int value) {
        return a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return a;
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof Const con)
            // not equals of toString()
            return toString().equals(con.toString());
        else
            return false;
    }
}