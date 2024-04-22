/**
 * Copy right 2024, Dongli Liu.
 * 
 * A class for solving the 24 game with recursive function.
 * 
 * The 24 Game is a mathematical card game where the objective is to use 
 * four given numbers and the basic arithmetic operations (+, -, *, /) to 
 * arrive at the number 24. Players must use all four numbers exactly once 
 * and can only use each number once. The game can be played competitively 
 * or as a solo puzzle. 
 * Example: given numbers 5, 12, 2, 10, the solution can be: (5+12)*2-10=24
 * 
 * @author Dongli Liu
 */

package main.utils;

import java.util.Set;
import java.util.ArrayList;

public class ExpressionNormalizer {
    public static String normalizeExpression(String expression, int left) {
        while (left >= 0 && expression.charAt(left) != '(')
            left--;
        if (left == -1)
            return expression;
        int right = getRightBracket(expression, left);
        if (isRemovable(expression, left, right))
            expression = removeTwoChars(expression, left, right);
        return normalizeExpression(expression, left - 1);
    }

    private static String removeTwoChars(String string, int left, int right) {
        String cleared = string.substring(0, left);
        cleared += string.substring(left + 1, right);
        if (right < string.length() - 1)
            cleared += string.substring(right + 1, string.length());
        return cleared;
    }

    private static boolean isRemovable(String expression, int left, int right) {
        Set<Character> flippingFactors = Set.of('-', '/');
        Set<Character> powerless = Set.of('+', '-', '(', ')');
        Set<Character> powerful = Set.of('*', '/');
        boolean frontOk = left == 0
                || (!flippingFactors.contains(expression.charAt(left - 1))
                        && !powerful.contains(expression.charAt(left - 1)))
                || (powerful.contains(getPivotSymbol(expression, left, right))
                        && expression.charAt(left - 1) == '-');
        boolean backOk = right == expression.length() - 1
                || powerful.contains(getPivotSymbol(expression, left, right))
                || powerless.contains(expression.charAt(right + 1));
        return frontOk && backOk;
    }

    private static char getPivotSymbol(String expression, int left, int right) {
        Set<Character> symbols = Set.of('+', '-', '*', '/');
        ArrayList<Character> operations = new ArrayList<Character>();
        for (int i = left + 1; i < right; i++) {
            char c = expression.charAt(i);
            if (symbols.contains(expression.charAt(i)))
                operations.add(c);
            if (expression.charAt(i) == '(')
                i = getRightBracket(expression, i);
        }
        int location = 0;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i) == '+' || operations.get(i) == '-') {
                location = i;
                break;
            }
        }
        return operations.get(location);
    }

    private static int getRightBracket(String expression, int left) {
        int right = left + 1;
        int nested = 0;
        while (right < expression.length()) {
            if (expression.charAt(right) == '(') {
                nested++;
            } else if (expression.charAt(right) == ')') {
                if (nested == 0)
                    return right;
                else
                    nested--;
            }
            right++;
        }
        return right;
    }
}
