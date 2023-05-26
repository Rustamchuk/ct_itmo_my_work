package expression.generic.expressionClasses;

public interface Expression<T> {
    T evaluate(T value);
    @Override
    String toString();
}
