package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.generic.expressionClasses.MyExpression;

import java.math.BigInteger;

public class BigIntegerEvaluator implements TypeEvaluator<BigInteger> {

    @Override
    public BigInteger add(BigInteger value1, BigInteger value2) {
        return value1.add(value2);
    }

    @Override
    public BigInteger subtract(BigInteger value1, BigInteger value2) {
        return value1.subtract(value2);
    }

    @Override
    public BigInteger multiply(BigInteger value1, BigInteger value2) {
        return value1.multiply(value2);
    }

    @Override
    public BigInteger divide(BigInteger value1, BigInteger value2) {
        if (value2.equals(BigInteger.ZERO)) throw new DivisionByZeroException();
        return value1.divide(value2);
    }

    @Override
    public BigInteger negate(BigInteger value) {
        return value.multiply(new BigInteger("-1"));
    }

    @Override
    public boolean isValidSym(char symbol) {
        return Character.isDigit(symbol);
    }

    @Override
    public BigInteger parse(String expression) {
        return new BigInteger(expression);
    }

    @Override
    public BigInteger parseNumber(int value) {
        return new BigInteger(String.valueOf(value));
    }
}
