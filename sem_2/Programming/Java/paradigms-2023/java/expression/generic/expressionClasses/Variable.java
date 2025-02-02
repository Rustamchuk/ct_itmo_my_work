package expression.generic.expressionClasses;


import java.util.Objects;

public class Variable<T> implements MyExpression<T> {
    String a;

    public Variable(String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return a;
    }

    @Override
    public T evaluate(T value) {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        if (a.equals("x"))
            return x;
        else if (a.equals("y"))
            return y;
        else
            return z;
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