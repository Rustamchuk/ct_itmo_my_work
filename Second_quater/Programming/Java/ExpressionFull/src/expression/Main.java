package expression;

import expression.exceptions.ExpressionParser;

import java.util.BitSet;

public class Main {
    public static void main(String[] args) throws Exception {
        ExpressionParser exp = new ExpressionParser();
        System.out.println(exp.parse("0").toString());
        System.out.println(Integer.toBinaryString(0));
    }
}