package expression.exceptions;

import expression.MyExpression;
import expression.Negate;

public class Set implements MyExpression {
    private final MyExpression a;

    public Set(MyExpression expression) {
        a = expression;
    }
    @Override
    public int evaluate(int value) {
        return a.evaluate(value);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return a.evaluate(x, y, z);
    }
    @Override
    public int hashCode() {
        return a.hashCode();
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof Set con)
            // not equals of toString()
            return a.equals(con.a);
        else
            return false;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + ")";
    }
}
