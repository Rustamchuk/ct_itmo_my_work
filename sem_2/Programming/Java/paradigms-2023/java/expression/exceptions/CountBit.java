package expression.exceptions;

import expression.MyExpression;

import java.util.Objects;

public class CountBit implements MyExpression {
    private final MyExpression a;
    public CountBit(MyExpression a) {
        this.a = a;
    }
    @Override
    public int evaluate(int value) {
        return count(a.evaluate(value));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return count(a.evaluate(x, y, z));
    }

    private int count(int ans) {
        int cnt = 0;
        String res = Integer.toBinaryString(ans);
        for (int i = 0; i < res.length(); i++) {
            if (res.charAt(i) == '1') {
                cnt++;
            }
        }
        return cnt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof CountBit con)
            // not equals of toString()
            return toString().equals(con.toString());
        else
            return false;
    }

    @Override
    public String toString() {
        return "count(" + a.toString() + ")";
    }
}
