package expression.generic;

import expression.generic.expressionClasses.MyExpression;

public class ShortEvaluator implements TypeEvaluator<Short> {
    @Override
    public Short add(Short value1, Short value2) {
        return (short) (value1 + value2);
    }

    @Override
    public Short subtract(Short value1, Short value2) {
        return (short) (value1 - value2);
    }

    @Override
    public Short multiply(Short value1, Short value2) {
        return (short) (value1 * value2);
    }

    @Override
    public Short divide(Short value1, Short value2) {
        return (short) (value1 / value2);
    }

    @Override
    public Short negate(Short value) {
        return (short) (value * -1);
    }

    @Override
    public boolean isValidSym(char symbol) {
        return Character.isDigit(symbol);
    }

    @Override
    public Short parse(String expression) {
        return Short.parseShort(expression);
    }

    @Override
    public Short parseNumber(int value) {
        return (short) value;
    }
}
