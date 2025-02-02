package expression.exceptions;

import expression.MyExpression;

import java.util.Objects;

public class CheckedNegate implements MyExpression {
    private final MyExpression a;
    public CheckedNegate(MyExpression a) {
        this.a = a;
    }
    @Override
    public int evaluate(int value) {
        return check(a.evaluate(value));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return check(a.evaluate(x, y, z));
    }

    private int check(int ans) {
        if (ans == Integer.MIN_VALUE) {
            throw new OverflowException(toString());
        }
        return ans * -1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof CheckedNegate con)
            // not equals of toString()
            return toString().equals(con.toString());
        else
            return false;
    }

    @Override
    public String toString() {
        return "-" + a;
    }
}
