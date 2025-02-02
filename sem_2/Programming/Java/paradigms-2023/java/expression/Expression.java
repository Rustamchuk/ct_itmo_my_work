package expression;

public interface Expression {
    int evaluate(int value);
    @Override
    String toString();
}
