package expression.generic;

import expression.generic.expressionClasses.MyExpression;

public interface TripleParser<T> {
    MyExpression<T> parse(String expression, TypeEvaluator<T> evaluator) throws Exception;
}
