package expression.exceptions;

public class NoValidMemberException extends NoValidException {
    public NoValidMemberException(String expr) {
        super("Parser didn't get valid member " + expr);
    }
}
