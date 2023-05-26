package expression.generic.expressionClasses;


public class Set<T> implements MyExpression<T> {
    private final MyExpression<T> a;

    public Set(MyExpression<T> expression) {
        a = expression;
    }
    @Override
    public T evaluate(T value) {
        return a.evaluate(value);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return a.evaluate(x, y, z);
    }
    @Override
    public int hashCode() {
        return a.hashCode();
    }

    @Override
    public boolean equals(Object exp) {
        if (exp.getClass() == this.getClass())
            // not equals of toString()
            return a.equals(((Set<?>) exp).a);
        else
            return false;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + ")";
    }
}
