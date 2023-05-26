package expression.exceptions;

import expression.MyExpression;
import expression.Operation;

public class ClearBit extends Operation {
    public ClearBit(MyExpression a, MyExpression b) {
        super(a, b, "clear");
    }

    @Override
    public int eval(int value1, int value2) {
        String res = Integer.toBinaryString(value1);
        value2 %= 32;
        if (value2 < 0)
            value2 = 32 + value2;
        value2 = res.length() - value2 - 1;
        if (value2 < 0) {
            return value1;
        }
        if (res.length() == 32 && value2 != 0)
            return Integer.MIN_VALUE + Integer.parseInt(
                    res.substring(1, value2) + "0" + res.substring(value2 + 1), 2);
        return Integer.parseInt(res.substring(0, value2) + "0" + res.substring(value2 + 1), 2);
    }
}
