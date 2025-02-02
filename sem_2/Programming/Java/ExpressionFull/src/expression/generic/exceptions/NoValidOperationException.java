package expression.generic.exceptions;

public class NoValidOperationException extends NoValidException {
    public NoValidOperationException(String expr) {
        super("Parser didn't get valid operation " + expr);
    }
}
