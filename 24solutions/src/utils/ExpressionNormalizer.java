package utils;

import java.util.Set;

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
        Set<Character> beforeOk = Set.of('(', '+');
        Set<Character> centerOk = Set.of('*', '/');
        Set<Character> afterOk = Set.of('+', '-', ')');
        boolean valid1 = left == 0 || beforeOk.contains(expression.charAt(left - 1)) ? true : false;
        boolean valid2 = centerOk.contains(getPivotSymbol(expression, left, right)) ? true : false;
        boolean valid3 = right == expression.length() - 1 || afterOk.contains(expression.charAt(right + 1)) ? true
                : false;
        return valid2 || (valid1 && valid3);
    }

    private static char getPivotSymbol(String expression, int left, int right) {
        Set<Character> symbols = Set.of('+', '-', '*', '/');
        for (int i = left + 1; i < right; i++) {
            char c = expression.charAt(i);
            if (symbols.contains(c))
                return c;
            if (c == '(') {
                while (i < right && expression.charAt(i) != ')')
                    i++;
            }
        }
        return ' ';
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
