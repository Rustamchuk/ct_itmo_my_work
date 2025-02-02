package expression;

public class Divide extends Operation {

    public Divide(MyExpression a, MyExpression b) {
        super(a, b, '/');
    }

    @Override
    public int eval(int value1, int value2) {
        return value1 / value2;
    }
}
