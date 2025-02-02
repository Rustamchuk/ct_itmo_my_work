package expression.exceptions;

import expression.MyExpression;
import expression.Operation;

public class CheckedDivide extends Operation {

    public CheckedDivide(MyExpression a, MyExpression b) {
        super(a, b, "/");
    }

    @Override
    public int eval(int value1, int value2) {
        if (value1 == Integer.MIN_VALUE && value2 == -1 || value2 == 0) throw new DivisionByZeroException();
        return value1 / value2;
    }
}