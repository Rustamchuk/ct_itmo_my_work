package expression.generic.expressionClasses;

import expression.generic.TypeEvaluator;

public class CheckedMultiply<T> extends Operation<T> {

    public CheckedMultiply(MyExpression<T> a, MyExpression<T> b, TypeEvaluator<T> evaluator) {
        super(a, b, evaluator, "*");
    }

    @Override
    public T eval(T value1, T value2) {
        return evaluator.multiply(value1, value2);
    }
}