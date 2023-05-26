package expression.generic;

import expression.generic.exceptions.DivisionByZeroException;
import expression.generic.exceptions.OverflowException;
import expression.generic.expressionClasses.MyExpression;

public class IntEvaluator implements TypeEvaluator<Integer> {
    @Override
    public Integer add(Integer value1, Integer value2) {
        if (
                (value1 > 0 && value2 > 0 && value1 > Integer.MAX_VALUE - value2) ||
                        (value1 < 0 && value2 < 0 && value1 < Integer.MIN_VALUE - value2)
        ) throw new OverflowException(toString());
        return value1 + value2;
    }

    @Override
    public Integer subtract(Integer value1, Integer value2) {
        if (
                (value1 >= 0 && value2 < 0 && value1 > Integer.MAX_VALUE + value2) ||
                        (value1 <= 0 && value2 > 0 && -1 * value2 < Integer.MIN_VALUE - value1)
        ) throw new OverflowException(toString());
        return value1 - value2;
    }

    @Override
    public Integer multiply(Integer value1, Integer value2) {
        if (
                (value1 > 0 && value2 > 0 && value1 > Integer.MAX_VALUE / value2) ||
                        (value1 < 0 && value2 < 0 && value1 < Integer.MAX_VALUE / value2) ||
                        (value1 < 0 && value2 > 0 && value1 < Integer.MIN_VALUE / value2) ||
                        (value1 > 0 && value2 < 0 && value2 < Integer.MIN_VALUE / value1)
        ) throw new OverflowException(toString());
        return value1 * value2;
    }

    @Override
    public Integer divide(Integer value1, Integer value2) {
        if (value1 == Integer.MIN_VALUE && value2 == -1 || value2 == 0) throw new DivisionByZeroException();
        return value1 / value2;
    }

    @Override
    public Integer negate(Integer value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException(toString());
        }
        return value * -1;
    }

    @Override
    public boolean isValidSym(char symbol) {
        return Character.isDigit(symbol);
    }

    @Override
    public Integer parse(String expression) {
        try {
            return Integer.parseInt(expression);
        } catch (NumberFormatException e) {
            throw new OverflowException("");
        }
    }

    @Override
    public boolean checkBorders(MyExpression<Integer> value) {
        return value.evaluate(0) == Integer.MIN_VALUE;
    }

    @Override
    public Integer checkBorders(String value) {
        if (value.equals(String.valueOf(Integer.MIN_VALUE)))
            return Integer.MIN_VALUE;
        return null;
    }

    @Override
    public Integer parseNumber(int value) {
        return value;
    }
}
