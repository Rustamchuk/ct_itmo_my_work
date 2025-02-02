package expression;

import expression.exceptions.OverflowException;

import java.util.Objects;

public class Negate implements MyExpression {
    private final MyExpression a;
    public Negate(MyExpression a) {
        this.a = a;
    }
    @Override
    public int evaluate(int value) {
        return check(a.evaluate(value)) * -1;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return check(a.evaluate(x, y, z)) * -1;
    }

    private int check(int ans) {
        if (ans == Integer.MIN_VALUE) throw new OverflowException(a.toString());
        return ans;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof Negate con)
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
