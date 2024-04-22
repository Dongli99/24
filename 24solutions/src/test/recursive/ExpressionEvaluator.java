package test.recursive;

import java.util.Stack;;

public class ExpressionEvaluator {

    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;
            if (Character.isDigit(tokens[i])) {
                StringBuilder sb = new StringBuilder();
                // There may be more than one digits in the number
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.'))
                    sb.append(tokens[i++]);
                values.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (tokens[i] == '(') { // If current token is an opening brace, push it to operators stack
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                // If current token is a closing brace, solve entire brace
                while (operators.peek() != '(')
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                operators.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // While top of 'operators' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'operators'
                // to top two elements in values stack
                while (!operators.empty() && hasPrecedence(tokens[i], operators.peek()))
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));

                // Push current token to 'operators'.
                operators.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining
        // operators to remaining values
        while (!operators.empty())
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));

        // Top of 'values' contains result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static double applyOperator(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new ArithmeticException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    public static void main(String[] args) {
        String expression = "11*6+6/3";
        System.out.println("Result: " + evaluate(expression));
    }
}
