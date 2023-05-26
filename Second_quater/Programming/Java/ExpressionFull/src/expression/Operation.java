package expression;

import java.util.Objects;

public abstract class Operation implements MyExpression {
    protected MyExpression a;
    protected MyExpression b;

    protected String operation;

    public Operation(MyExpression a, MyExpression b, String operation) {
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    @Override
    public boolean equals(Object exp) {
        if (exp instanceof Operation oper)
            return a.equals(oper.getA()) && b.equals(oper.getB())
                    && operation.equals(oper.getOperation());
        else
            return false;
    }

    public int hashCode() {
        return Objects.hash(a, b, operation);
    }

    public Expression getA() {
        return a;
    }

    public Expression getB() {
        return b;
    }

    public String getOperation() { return operation; }

    @Override
    public int evaluate(int value) {
        return eval(a.evaluate(value), b.evaluate(value));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return eval(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }

    protected abstract int eval(int value1, int value2);

    @Override
    public String toString() {
        return '(' +
                a.toString() +
                " " + operation + " " +
                b.toString() +
                ')';
    }
}
