package expression;

public class Subtract extends Operation {

    public Subtract(MyExpression a, MyExpression b) {
        super(a, b, '-');
    }

    public int eval(int value1, int value2) {
        return value1 - value2;
    }
}
