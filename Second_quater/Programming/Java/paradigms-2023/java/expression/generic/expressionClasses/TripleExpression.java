package expression.generic.expressionClasses;

public interface TripleExpression<T> {
    T evaluate(T x, T y, T z);
}
