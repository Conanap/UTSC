package booleanoo;

import java.util.HashMap;
import java.util.Map;

public class BooleanEval {

  /**
   * An example of using the hierarchy of Boolean Expressions.
   * 
   * @param args as usual
   */
  public static void main(String[] args) {

    Map<String, Boolean> context = new HashMap<>();
    context.put("a", true);
    context.put("b", false);
    context.put("c", false);
    context.put("d", true);

    // ((a iff (not b)) implies ((true and c) iff ((not b) and c)))
    BooleanExpression expr = new Implication(
        new IffExpression(new Variable("a"), new Negation(new Variable("b"))),
        new IffExpression(
            new Conjunction(new BooleanValue(true), new Variable("c")),
            new Conjunction(new Negation(new Variable("b")),
                new Variable("c"))));

    System.out.println(expr);
    System.out.println(context);
    try {
      System.out.println(expr.evaluate(context));
    } catch (UnassignedVariableException exn) {
      System.out.println("I should not be here.");
    }
    System.out.println(expr.simplify(context));

    Map<String, Boolean> context2 = new HashMap<>();
    context2.put("c", false);
    context2.put("d", true);

    // ((not (not (a and c))) or b)
    BooleanExpression expr2 = new Disjunction(
        new Negation(new Negation(
            new Conjunction(new Variable("a"), new Variable("c")))),
        new Variable("b"));

    System.out.println(expr2);
    System.out.println(context2);
    try {
      System.out.println(expr2.evaluate(context2));
    } catch (UnassignedVariableException exn) {
      System.out.println("Good, I'm here.");
    }
    System.out.println(expr2.simplify(context2));
  }
  /*
   * ((a iff (not b)) implies ((true and c) iff ((not b) and c))) {a=true,
   * b=false, c=false, d=true} true true ((not (not (a and c))) or b) {c=false,
   * d=true} Good, I'm here. b
   */
}
