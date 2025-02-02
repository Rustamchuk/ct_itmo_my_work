package expression.generic.expressionClasses;

import expression.generic.TypeEvaluator;

import java.util.Objects;

public class CheckedNegate<T> implements MyExpression<T> {
    private final MyExpression<T> a;
    private final TypeEvaluator<T> evaluator;
    public CheckedNegate(MyExpression<T> a, TypeEvaluator<T> evaluator) {
        this.a = a;
        this.evaluator = evaluator;
    }
    @Override
    public T evaluate(T value) {
        return check(a.evaluate(value));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return check(a.evaluate(x, y, z));
    }

    private T check(T ans) {
        return evaluator.negate(ans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public boolean equals(Object exp) {
        if (exp.getClass() == this.getClass())
            // not equals of toString()
            return toString().equals(exp.toString());
        else
            return false;
    }

    @Override
    public String toString() {
        return "-" + a;
    }
}
