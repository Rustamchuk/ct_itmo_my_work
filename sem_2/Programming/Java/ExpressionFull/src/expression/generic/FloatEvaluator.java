package expression.generic;

import expression.generic.expressionClasses.MyExpression;

public class FloatEvaluator implements TypeEvaluator<Float> {
    @Override
    public Float add(Float value1, Float value2) {
        return value1 + value2;
    }

    @Override
    public Float subtract(Float value1, Float value2) {
        return value1 - value2;
    }

    @Override
    public Float multiply(Float value1, Float value2) {
        return value1 * value2;
    }

    @Override
    public Float divide(Float value1, Float value2) {
        return value1 / value2;
    }

    @Override
    public Float negate(Float value) {
        return value * -1;
    }

    @Override
    public boolean isValidSym(char symbol) {
        return Character.isDigit(symbol) || ".,eE".contains(String.valueOf(symbol));
    }

    @Override
    public Float parse(String expression) {
        return Float.parseFloat(expression);
    }

    @Override
    public Float parseNumber(int value) {
        return (float) value;
    }
}
