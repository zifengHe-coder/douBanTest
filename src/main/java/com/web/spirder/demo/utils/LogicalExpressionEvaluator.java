package com.web.spirder.demo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author hezifeng
 * @create 2023/4/3 10:08
 */
public class LogicalExpressionEvaluator {
    public static boolean evaluate(String expression) {
        String postfix = infixToPostfix(expression);
        Stack<Boolean> stack = new Stack<>();
        for (char ch : postfix.toCharArray()) {
            if (ch == '|') {
                boolean right = stack.pop();
                boolean left = stack.pop();
                stack.push(left || right);
            } else if (ch == '&') {
                boolean right = stack.pop();
                boolean left = stack.pop();
                stack.push(right && left);
            } else if (Character.isUpperCase(ch)) {
                stack.push(getVariableValue(ch));
            }
        }
        return stack.pop();
    }

    //逆波兰表达式
    public static String infixToPostfix(String infix) {
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('|',1);
        precedence.put('&',2);
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (char ch : infix.toCharArray()) {
            if (ch == ' ' || ch == '!') {//取非直接跳过，让最后公式结果带入变量=1后结果均为true
                continue;
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop(); //pop '('
            } else if (ch == '|' || ch == '&') {
                while (!stack.isEmpty() && stack.peek() != '(' && precedence.get(ch) <= precedence.get(stack.peek())) {
                    postfix.append(stack.pop());
                }
                stack.push(ch);
            } else if (Character.isUpperCase(ch)) {
                postfix.append(ch);
            } else {
                throw new IllegalArgumentException("Invalid character: " + ch);
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    private static boolean getVariableValue(char ch) {
        return true;
    }

    public static boolean checkLogicalExpression(String expression) {
        String regex = "^\\(*[A-Z](\\s*[|&]\\s*\\(*[A-Z]\\)*)*\\)*$";
        return expression.matches(regex);
    }
}
