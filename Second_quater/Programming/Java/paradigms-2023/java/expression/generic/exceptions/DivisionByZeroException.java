package expression.generic.exceptions;

public class DivisionByZeroException extends ArithmeticException {
    public DivisionByZeroException() {
        super("division by zero");
    }
}
