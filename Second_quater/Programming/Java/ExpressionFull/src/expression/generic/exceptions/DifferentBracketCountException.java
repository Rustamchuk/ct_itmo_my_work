package expression.generic.exceptions;

public class DifferentBracketCountException extends ParsingException {
    public DifferentBracketCountException(String expr) {
        super("Different Bracket count " + expr);
    }
}