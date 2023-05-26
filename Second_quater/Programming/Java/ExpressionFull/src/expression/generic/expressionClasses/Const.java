package expression.generic.expressionClasses;


import java.util.Objects;

public class Const<T> implements MyExpression<T> {
    private final T a;
    public Const(T a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return String.valueOf(a);
    }

    @Override
    public T evaluate(T value) {
        return a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return a;
    }

    @Override
    public boolean equals(Object exp) {
        if (exp.getClass() == this.getClass())
            // not equals of toString()
            return toString().equals(exp.toString());
        else
            return false;
    }
}