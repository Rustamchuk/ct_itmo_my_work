package expression.exceptions;

import expression.MyExpression;
import expression.Operation;

public class CheckedSubtract extends Operation {

    public CheckedSubtract(MyExpression a, MyExpression b) {
        super(a, b, "-");
    }

    public int eval(int value1, int value2) {
        if (
                (value1 >= 0 && value2 < 0 && value1 > Integer.MAX_VALUE + value2) ||
                (value1 <= 0 && value2 > 0 && -1 * value2 < Integer.MIN_VALUE - value1)
        ) throw new OverflowException(toString());
        return value1 - value2;
    }
}