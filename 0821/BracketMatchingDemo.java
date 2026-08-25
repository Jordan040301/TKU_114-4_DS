import java.util.ArrayDeque;
import java.util.Deque;

public class BracketMatchingDemo {
    static boolean isBalanced(String expression) {
        if (expression == null) {
            return false;
        }

        Deque<Character> stack = new ArrayDeque<>();
        for (char symbol : expression.toCharArray()) {
            if (symbol == '(' || symbol == '[' || symbol == '{') {
                stack.push(symbol);
            } else if (symbol == ')' || symbol == ']' || symbol == '}') {
                if (stack.isEmpty() || !matches(stack.pop(), symbol)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    static boolean matches(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '[' && close == ']')
                || (open == '{' && close == '}');
    }

    public static void main(String[] args) {
        String[] expressions = {
            "{[()]}", "([)]", "(()", "a + (b * c)", ""
        };

        for (String expression : expressions) {
            System.out.println(expression + " -> " + isBalanced(expression));
        }
    }
}