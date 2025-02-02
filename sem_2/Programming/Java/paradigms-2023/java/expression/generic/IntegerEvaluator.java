package expression.generic;

import expression.generic.expressionClasses.MyExpression;

public class IntegerEvaluator implements TypeEvaluator<Integer> {
    @Override
    public Integer add(Integer value1, Integer value2) {
        return value1 + value2;
    }

    @Override
    public Integer subtract(Integer value1, Integer value2) {
        return value1 - value2;
    }

    @Override
    public Integer multiply(Integer value1, Integer value2) {
        return value1 * value2;
    }

    @Override
    public Integer divide(Integer value1, Integer value2) {
        return value1 / value2;
    }

    @Override
    public Integer negate(Integer value) {
        return value * -1;
    }

    @Override
    public boolean isValidSym(char symbol) {
        return Character.isDigit(symbol);
    }

    @Override
    public Integer parse(String expression) {
        return Integer.parseInt(expression);
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
