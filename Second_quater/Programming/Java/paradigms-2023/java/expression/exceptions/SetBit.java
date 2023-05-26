package expression.exceptions;

import expression.MyExpression;
import expression.Operation;

public class SetBit extends Operation {
    public SetBit(MyExpression a, MyExpression b) {
        super(a, b, "set");
    }

    @Override
    public int eval(int value1, int value2) {
        String res = Integer.toBinaryString(value1);
        value2 %= 32;
        if (value2 < 0)
            value2 = 32 + value2;
        value2 = res.length() - value2 - 1;
        if (value2 < 0) {
            res = "0".repeat(value2 * -1 - 1) + res;
            if (res.length() == 31)
                return Integer.MIN_VALUE + Integer.parseInt(res, 2);
            return Integer.parseInt("1" + res, 2);
        }
        if (res.charAt(value2) == '1') return value1;
        if (res.length() == 32) {
            return Integer.MIN_VALUE + Integer.parseInt(res.substring(1, value2)
                    + "1" + res.substring(value2 + 1), 2);
        }
        return Integer.parseInt(res.substring(0, value2)
                + "1" + res.substring(value2 + 1), 2);
    }
}
