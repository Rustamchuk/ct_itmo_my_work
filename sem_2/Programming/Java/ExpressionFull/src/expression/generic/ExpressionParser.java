package expression.generic;

import expression.generic.exceptions.DifferentBracketCountException;
import expression.generic.exceptions.NoValidMemberException;
import expression.generic.exceptions.NoValidOperationException;
import expression.generic.exceptions.ParsingException;
import expression.generic.expressionClasses.*;

public class ExpressionParser<T> implements TripleParser<T> {
    private StringSourse expression;
    private TypeEvaluator<T> evaluator;
    private char cur;

    @Override
    public MyExpression<T> parse(String expression, TypeEvaluator<T> evaluator) throws ParsingException {
        this.expression = new StringSourse(expression);
        this.evaluator = evaluator;
        return buildExpression(false);
    }

    private MyExpression<T> buildExpression(boolean set) throws ParsingException {
        MyExpression<T> currentExpression = getOperation(getMember(false), false, set);
        while ((set || expression.hasNext()) && (!set || cur != ')')) {
            currentExpression = getOperation(currentExpression, false, set);
        }
        cur = 0;
        return currentExpression;
    }

    private MyExpression<T> getMember(boolean negate) throws ParsingException {
        if (expression.hasNext()) skipWhiteSpace();
        else throw new NoValidMemberException(expression.errorMessage());

        if (cur == 'x') {
            return new Variable<>("x");
        } else if (cur == 'y') {
            return new Variable<>("y");
        } else if (cur == 'z') {
            return new Variable<>("z");
        } else if (cur == '-') {
            MyExpression<T> res = getMember(true);
            if (res instanceof CheckedNegate<T>)
                return new CheckedNegate<>(new Set<>(res), evaluator);
            if (res instanceof Const<T>) {
                if (evaluator.checkBorders(res)) {
                    if (negate) return new Set<>(res);
                    else return res;
                }
            }
            return new CheckedNegate<>(res, evaluator);
        } else if (cur == '(') {
            MyExpression<T> res = buildExpression(true);
            if (negate)
                return new Set<>(res);
            return res;
        } else if (Character.isDigit(cur)) {
            return new Const<>(convertToNumber(negate));
        }
        throw new NoValidMemberException(expression.errorMessage());
    }

    private MyExpression<T> getOperation(MyExpression<T> member, boolean onlyPrimaryOperations, boolean set) throws ParsingException {
        if (expression.hasNext()) skipWhiteSpace();
        else if (!set) return member;
        else throw new DifferentBracketCountException(expression.errorMessage());

        if (cur == ' ')
            return member;

        MyExpression<T> find = getPrimaryOperation(member);
        if (find == null) {
            find = getStandardOperation(member, onlyPrimaryOperations, set);
            if (find == null) {
                throw new NoValidOperationException(expression.errorMessage());
            }
            return find;
        }
        find = getOperation(find, true, set);
        return find;
    }

    private MyExpression<T> getPrimaryOperation(MyExpression<T> member)  throws ParsingException {
        if (cur == '*') {
            return new CheckedMultiply<>(member, getMember(false), evaluator);
        } else if (cur == '/') {
            return new CheckedDivide<>(member, getMember(false), evaluator);
        }
        return null;
    }

    private MyExpression<T> getStandardOperation(MyExpression<T> member, boolean onlyPrimaryOperations, boolean set)  throws ParsingException {
        if (cur == '+') {
            if (!onlyPrimaryOperations) {
                return new CheckedAdd<>(member, getOperation(getMember(false), true, set), evaluator);
            } else {
                expression.back();
                return member;
            }
        } else if (cur == '-') {
            if (!onlyPrimaryOperations) {
                return new CheckedSubtract<>(member, getOperation(getMember(false), true, set), evaluator);
            } else {
                expression.back();
                return member;
            }
        } else if (cur == ')') {
            if (!set) throw new DifferentBracketCountException(expression.errorMessage());
            return member;
        }
        return null;
    }

    private T convertToNumber(boolean negate) {
        StringBuilder number = new StringBuilder();
        while (evaluator.isValidSym(cur)) {
            number.append(cur);
            if (expression.hasNext())
                cur = expression.next();
            else
                cur = 0;
        }
        if (cur != 0) expression.back();
        cur = 0;
        if (negate) {
            T out = evaluator.checkBorders("-" + number);
            if (out != null) return out;
        }
        return evaluator.parse(number.toString());
    }

    private void skipWhiteSpace() {
        cur = expression.next();
        while (expression.hasNext() &&
                (Character.isWhitespace(cur) || cur == '\r' || cur == '\n' || cur == '\t')
        ) cur = expression.next();
    }
}
