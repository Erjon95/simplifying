import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solution {
    private static StringBuilder coefficient;

    public static String simplify(String[] examples, String formula) {
        coefficient = new StringBuilder();

        Map<String, String> equations = new HashMap<>();

        Arrays.stream(examples)
              .forEach(equation -> {
                  String[] a = equation.split("=");
                  equations.put(a[1].trim(), a[0].trim());
              });

        Stack<String> stack = new Stack<>();

        formula.chars()
               .forEach(i -> {
                   if (i >= 48 && i <= 57)
                       coefficient.append((char)i);
                   else{
                       if (coefficient.length() > 0) {
                           stack.push(coefficient.toString());
                           stack.push(String.valueOf((char)i));
                           coefficient = new StringBuilder();
                       }else
                           stack.push(String.valueOf((char)i));
                   }
               });


        return process(equations, stack);
    }

    private static String process(Map<String, String> equations, Stack<String> stack){
        
    }

}
