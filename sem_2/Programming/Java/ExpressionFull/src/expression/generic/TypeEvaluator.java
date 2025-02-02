package expression.generic;

import expression.generic.expressionClasses.MyExpression;

public interface TypeEvaluator<T> {
    T add(T value1, T value2);

    T subtract(T value1, T value2);

    T multiply(T value1, T value2);

    T divide(T value1, T value2);

    T negate(T value);

    boolean isValidSym(char symbol);

    T parse(String expression);

    default boolean checkBorders(MyExpression<T> value) {
        return false;
    }

    default T checkBorders(String value) {
        return null;
    }

    T parseNumber(int value);
}
