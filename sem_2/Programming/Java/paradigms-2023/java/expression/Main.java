package expression;

import expression.exceptions.ExpressionParser;

public class Main {
    public static void main(String[] args) throws Exception {
        ExpressionParser exp = new ExpressionParser();
        System.out.println(exp.parse("(\t  (y set x)\u2029set\n" +
                " \u000B\u2029\f\u000B\u000B(z set z)\f\f\f)").evaluate(0, 0, 0));
        System.out.println(Integer.toBinaryString(2));
        System.out.println(1%32);
    }
}