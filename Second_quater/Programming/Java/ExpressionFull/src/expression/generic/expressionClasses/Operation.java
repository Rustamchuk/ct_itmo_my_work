package expression.generic.expressionClasses;

import expression.generic.TypeEvaluator;

import java.util.Objects;

public abstract class Operation<T> implements MyExpression<T> {
    protected final MyExpression<T> a;
    protected final MyExpression<T> b;
    protected final TypeEvaluator<T> evaluator;

    protected final String operation;

    public Operation(MyExpression<T> a, MyExpression<T> b, TypeEvaluator<T> evaluator, String operation) {
        this.a = a;
        this.b = b;
        this.evaluator = evaluator;
        this.operation = operation;
    }

    @Override
    public boolean equals(Object exp) {
        if (exp.getClass() == this.getClass())
            return a.equals(getA()) && b.equals(((Operation<?>) exp).getB())
                    && operation.equals(((Operation<?>) exp).getOperation());
        else
            return false;
    }

    public int hashCode() {
        return Objects.hash(a, b, operation);
    }

    public Expression<T> getA() {
        return a;
    }

    public Expression<T> getB() {
        return b;
    }

    public String getOperation() { return operation; }

    @Override
    public T evaluate(T value) {
        return eval(a.evaluate(value), b.evaluate(value));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return eval(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }

    protected abstract T eval(T value1, T value2);

    @Override
    public String toString() {
        return '(' +
                a.toString() +
                " " + operation + " " +
                b.toString() +
                ')';
    }
}
