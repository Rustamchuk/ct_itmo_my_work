package expression.exceptions;

import expression.MyExpression;

public class ExpressionParser implements TripleParser {
    private StringSourse expression;
    private char cur;

    @Override
    public MyExpression parse(String expression) throws ParsingException {
        this.expression = new StringSourse(expression);
        return buildExpression(false);
    }

    private MyExpression buildExpression(boolean set) throws ParsingException {
        MyExpression currentExpression = getOperation(getMember(false), false, set);
        while ((set || expression.hasNext()) && (!set || cur != ')')) {
            currentExpression = getOperation(currentExpression, false, set);
        }
        cur = 0;
        return currentExpression;
    }

    private MyExpression getMember(boolean negate) throws ParsingException {
        if (expression.hasNext()) skipWhiteSpace();
        else throw new NoValidMemberException(expression.errorMessage());

        if (cur == 'x') {
            return new Variable("x");
        } else if (cur == 'y') {
            return new Variable("y");
        } else if (cur == 'z') {
            return new Variable("z");
        } else if (cur == '-') {
            MyExpression res = getMember(true);
            if (res instanceof CheckedNegate)
                return new CheckedNegate(new Set(res));
            if (res instanceof Const con) {
                if (con.evaluate(0) == Integer.MIN_VALUE) {
                    if (negate) return new Set(res);
                    else return res;
                }
            }
            return new CheckedNegate(res);
        } else if (cur == '(') {
            MyExpression res = buildExpression(true);
            if (negate)
                return new Set(res);
            return res;
        } else if (Character.isDigit(cur)) {
            return new Const(convertToNumber(negate));
        } else if (cur == 'c') {
            if (expression.isUnicWord("count")) {
                return new CountBit(getMember(negate));
            }
            throw new NoValidException("where count " + expression.errorMessage());
        }
        throw new NoValidMemberException(expression.errorMessage());
    }

    private MyExpression getOperation(MyExpression member, boolean onlyPrimaryOperations, boolean set) throws ParsingException {
        if (expression.hasNext()) skipWhiteSpace();
        else if (!set) return member;
        else throw new DifferentBracketCountException(expression.errorMessage());

        if (cur == ' ')
            return member;

        MyExpression find = getPrimaryOperation(member);
        if (find == null) {
            find = getStandardOperation(member, onlyPrimaryOperations, set);
            if (find == null) {
                if (cur == 's') {
                    if (expression.isUnicWord("set")) {
                        return new SetBit(member, getMember(false));
                    }
                }
                if (cur == 'c') {
                    if (expression.isUnicWord("clear")) {
                        return new ClearBit(member, getMember(false));
                    }
                }
                throw new NoValidOperationException(expression.errorMessage());
            }
            return find;
        }
        find = getOperation(find, true, set);
        return find;
    }

    private MyExpression getPrimaryOperation(MyExpression member)  throws ParsingException {
        if (cur == '*') {
            return new CheckedMultiply(member, getMember(false));
        } else if (cur == '/') {
            return new CheckedDivide(member, getMember(false));
        }
        return null;
    }

    private MyExpression getStandardOperation(MyExpression member, boolean onlyPrimaryOperations, boolean set)  throws ParsingException {
        if (cur == '+') {
            if (!onlyPrimaryOperations) {
                return new CheckedAdd(member, getOperation(getMember(false), true, set));
            } else {
                expression.back();
                return member;
            }
        } else if (cur == '-') {
            if (!onlyPrimaryOperations) {
                return new CheckedSubtract(member, getOperation(getMember(false), true, set));
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

    private int convertToNumber(boolean negate) {
        StringBuilder number = new StringBuilder();
        while (Character.isDigit(cur)) {
            number.append(cur);
            if (expression.hasNext())
                cur = expression.next();
            else
                cur = 0;
        }
        if (cur != 0) expression.back();
        cur = 0;
        if (negate && ("-" + number).equals(String.valueOf(Integer.MIN_VALUE))) {
            return Integer.MIN_VALUE;
        }
        try {
            return Integer.parseInt(number.toString());
        } catch (NumberFormatException e) {
            throw new OverflowException(expression.errorMessage());
        }
    }

    private void skipWhiteSpace() {
        cur = expression.next();
        while (expression.hasNext() &&
                (Character.isWhitespace(cur) || cur == '\r' || cur == '\n' || cur == '\t')
        ) cur = expression.next();
    }
}
