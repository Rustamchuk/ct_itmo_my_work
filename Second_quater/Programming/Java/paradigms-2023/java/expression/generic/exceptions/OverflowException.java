package expression.generic.exceptions;

public class OverflowException extends ArithmeticException{
    public OverflowException(String expr) {
        super("overflow " + expr);
    }
}