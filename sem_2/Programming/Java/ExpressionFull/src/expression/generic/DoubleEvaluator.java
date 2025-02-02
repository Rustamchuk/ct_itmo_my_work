package expression.generic;

import expression.generic.expressionClasses.MyExpression;

public class DoubleEvaluator implements TypeEvaluator<Double> {

    @Override
    public Double add(Double value1, Double value2) {
        return value1 + value2;
    }

    @Override
    public Double subtract(Double value1, Double value2) {
        return value1 - value2;
    }

    @Override
    public Double multiply(Double value1, Double value2) {
        return value1 * value2;
    }

    @Override
    public Double divide(Double value1, Double value2) {
//        if (value2 == 0) throw new DivisionByZeroException();
        return value1 / value2;
    }

    @Override
    public Double negate(Double value) {
        return value * -1;
    }

    @Override
    public boolean isValidSym(char symbol) {
        return Character.isDigit(symbol) || ".,eE".contains(String.valueOf(symbol));
    }

    @Override
    public Double parse(String expression) {
        return Double.parseDouble(expression);
    }

    @Override
    public Double parseNumber(int value) {
        return (double) value;
    }
}
