import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solution {
    private static StringBuilder coefficient, variable;
    private static String var;
    private static boolean isReducible;
    private static int sign, numberOfMinuses, co, result;

    public static String simplify(String[] examples, String formula) {
        final StringBuilder s = new StringBuilder();

        final Map<String, String> equations = new HashMap<>();

        Arrays.stream(examples)
              .forEach(equation -> {
                  String[] a = equation.split("=");
                  equations.put(a[1].trim(), a[0].trim());
              });

        //remove whitespace
        formula.chars()
               .filter(i -> i != 32)
               .forEach(i -> s.append((char)i));

        return evaluate(s, equations);
    }

    private static String reduce(StringBuilder s, Map<String, String> equations){
        StringBuilder s1 = new StringBuilder();
        variable = new StringBuilder();

        isReducible = false;

        s.chars()
         .forEach(i -> {
             if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122))
                 variable.append((char) i);
             else {
                 if (variable.length() > 0) {
                     if (equations.containsKey(variable.toString())) {
                         s1.append('(');
                         s1.append(equations.get(variable.toString()));
                         s1.append(')');

                         isReducible = true;
                     }
                     else s1.append(variable);
                     variable = new StringBuilder();
                 }
                 s1.append((char) i);
             }
         });

        if (variable.length() > 0) {
            if (equations.containsKey(variable.toString())) {
                s1.append('(');
                s1.append(equations.get(variable.toString()));
                s1.append(')');
                isReducible = true;
            }
            else s1.append(variable);
        }

        if (!isReducible) {
            variable = new StringBuilder();
            return s1.toString();
        }

        return reduce(s1, equations);
    }

    private static String removeBraces(StringBuilder s, Map<String, String> equations){
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        coefficient = new StringBuilder();
        Stack<Integer> stack = new Stack<>();
        sign = 43;
        variable = new StringBuilder();

        //remove whitespace
        reduce(s, equations).chars()
          .filter(i -> i != 32)
          .forEach(i -> stringBuilder.append((char)i));

        stringBuilder.chars()
                     .forEach(i -> {
                         if (i >= 48 && i <= 57)
                             coefficient.append((char) i);
                         else if (i == 43 || i == 45){
                             if (!variable.isEmpty()) {
                                 result.append(variable);
                                 variable = new StringBuilder();
                             }
                             sign = i;
                         }else if (i == 40){
                             if (!coefficient.isEmpty()) {
                                 stack.push(sign == 43 ? Integer.parseInt(coefficient.toString()) : Integer.parseInt(coefficient.toString()) * -1);
                                 coefficient = new StringBuilder();
                             }else
                                 stack.push(sign == 43 ? 1 : -1);
                             sign = 43;
                         }else if (i == 41) {
                             if (!variable.isEmpty()){
                                 result.append(variable);
                                 variable = new StringBuilder();
                             }
                             stack.pop();
                         } else {
                             if (!coefficient.isEmpty()){
                                 result.append((char) sign);
                                 result.append(stack.stream().reduce(1, (a, b) -> a * b) * Integer.parseInt(coefficient.toString()));
                                 coefficient = new StringBuilder();
                             }else {
                                 result.append((char) sign);
                                 result.append(stack.stream().reduce(1, (a, b) -> a * b));
                             }
                             variable.append((char) i);
                         }
                     });

        if (!variable.isEmpty())
            result.append(variable);

        return result.toString();
    }

    private static String evaluate(StringBuilder s, Map<String, String> equations){

        numberOfMinuses = result = 0;

        String s1 = removeBraces(s, equations);

        coefficient = new StringBuilder();
        variable = new StringBuilder();

        s1.chars()
          .forEach(i -> {
              if (i == 45 || i == 43) {
                  numberOfMinuses = i == 45 ? numberOfMinuses + 1 : numberOfMinuses;
                  if (!variable.isEmpty()) {
                      var = variable.toString();
                      variable = new StringBuilder();
                  }
              } else if (i >= 48 && i <= 57){
                  co = numberOfMinuses % 2 == 0 ? 1 : -1;
                  coefficient.append((char)i);
              } else {
                  result += co * Integer.parseInt(coefficient.toString());
                  numberOfMinuses = 0;
                  coefficient = new StringBuilder();
                  variable.append((char)i);
              }
          });

        if (!variable.isEmpty())
            var = variable.toString();

        return (result + var);
    }

    public static void main(String[] args) {
        String[] a = new String[]{"3q = A", "6q + 5A = r", "-6q + 3r = c", "-3q + 9A + 7r = t", "0q + 9A + 6r + 0c - 0t = F", "1q + 4A - 5r + 3c - 9F = N"};
        System.out.println(Solution.simplify(a, "2(1r - 2r - 1F) - 2r - 2(6(6N - 2c - 1t) + 1N + 4r) + 2(1F + 4N) + 4F + 1(-2r + 4c)"));
    }
}
