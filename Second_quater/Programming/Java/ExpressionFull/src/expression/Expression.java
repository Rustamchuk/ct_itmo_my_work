package expression;

import java.util.Objects;

public interface Expression {
    int evaluate(int value);
    @Override
    String toString();
}
