package expression.generic;

import expression.generic.exceptions.ParsingException;
import expression.generic.expressionClasses.MyExpression;

public class GenericTabulator implements Tabulator {
    private String expression;

    @Override
    public Object[][][] tabulate(String mode, String expression,
                                 int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        TypeEvaluator<?> type = switch (mode) {
            case "i" -> new IntEvaluator();
            case "d" -> new DoubleEvaluator();
            case "u" -> new IntegerEvaluator();
            case "f" -> new FloatEvaluator();
            case "s" -> new ShortEvaluator();
            default -> new BigIntegerEvaluator();
        };
        this.expression = expression;

        return parse(type, x1, x2, y1, y2, z1, z2);
    }

    public <T> Object[][][] parse(TypeEvaluator<T> type,
                                  int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {

        Object[][][] results = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        ExpressionParser<T> parser = new ExpressionParser<>();

        MyExpression<T> resultExpression = parser.parse(expression, type);

        for (int x = 0; x < results.length; x++) {
            for (int y = 0; y < results[0].length; y++) {
                for (int z = 0; z < results[0][0].length; z++) {
                    try {
                        results[x][y][z] = resultExpression.
                                evaluate(
                                        type.parseNumber(x1 + x),
                                        type.parseNumber(y1 + y),
                                        type.parseNumber(z1 + z)
                                );
                    } catch (ArithmeticException ignored) {
                    }
                }
            }
        }
        return results;
    }
}
