package expression.generic.expressionClasses;

import expression.generic.TypeEvaluator;

public class CheckedDivide<T> extends Operation<T> {

    public CheckedDivide(MyExpression<T> a, MyExpression<T> b, TypeEvaluator<T> evaluator) {
        super(a, b, evaluator, "/");
    }

    @Override
    public T eval(T value1, T value2) {
        return evaluator.divide(value1, value2);
    }
}