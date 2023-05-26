package expression.exceptions;

import expression.MyExpression;
import expression.TripleExpression;

public interface TripleParser {
    TripleExpression parse(String expression) throws Exception;
}
