package expression.generic.expressionClasses;

import expression.generic.TypeEvaluator;

public class CheckedSubtract<T> extends Operation<T> {

    public CheckedSubtract(MyExpression<T> a, MyExpression<T> b, TypeEvaluator<T> evaluator) {
        super(a, b, evaluator, "-");
    }

    public T eval(T value1, T value2) {
        return evaluator.subtract(value1, value2);
    }
}