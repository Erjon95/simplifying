import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solution {
    private static int totalCo;
    private static StringBuilder coefficient, variable;
    private static String var;
    private static boolean isReducible;
    private static int sign, numberOfMinuses, co;

    public static void simplify(String[] examples, String formula) {
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

        System.out.println(reduce(s, equations));
        System.out.println(removeBraces(s, equations));
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
        totalCo = 1;
        sign = 43;

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
                             //result.append((char)i);
                             sign = i;
                         }else if (i == 40){
                             if (!coefficient.isEmpty()) {
                                 totalCo = sign == 43 ? totalCo * Integer.parseInt(coefficient.toString()) : totalCo * Integer.parseInt(coefficient.toString()) * -1;
                                 stack.push(sign == 43 ? Integer.parseInt(coefficient.toString()) : Integer.parseInt(coefficient.toString()) * -1);
                                 coefficient = new StringBuilder();
                             }else {
                                 stack.push(sign == 43 ? 1 : -1);
                                 totalCo *= stack.peek();
                             }
                             sign = 43;
                         }else if (i == 41) {
                             if (!variable.isEmpty()){
                                 result.append(variable);
                                 variable = new StringBuilder();
                             }
                             totalCo /= stack.pop();
                         }
                         else {
                             if (!coefficient.isEmpty()){
                                 result.append((char) sign);
                                 result.append(totalCo * Integer.parseInt(coefficient.toString()));
                                 coefficient = new StringBuilder();
                             }else {
                                 result.append((char) sign);
                                 result.append(totalCo);
                             }
                             variable.append((char) i);
                         }
                     });

        if (!variable.isEmpty())
            result.append(variable);

        return result.toString();
    }

    private static String evaluate(StringBuilder s, Map<String, String> equations){

        numberOfMinuses = 0;

        String s1 = removeBraces(s, equations);

        coefficient = new StringBuilder();
        variable = new StringBuilder();

        s1.chars()
          .forEach(i -> {
              if (i == 45 || i == 43) {
                  numberOfMinuses = i == 45 ? numberOfMinuses + 1 : numberOfMinuses;
                  if (!variable.isEmpty())
                      var = variable.toString();
              } else if (i >= 48 && i <= 57){
                  co = numberOfMinuses % 2 == 0 ? 1 : -1;
                  coefficient.append((char)i);
              } else {
                  co = co * Integer.parseInt(coefficient.toString());
                  variable.append((char)i);
              }
          });

    }

    public static void main(String[] args) {
        String[] a = new String[]{"a + 3g = k", "-70a = g"};
        Solution.simplify(a, "-k + a");

    }
}
