package booleanoo;

import java.util.Map;

/**
 * Interface for all boolean expressions.
 */
public interface BooleanExpression {

  /**
   * Evaluate the boolean expression.
   * 
   * @param context A map of variable names to its boolean value.
   * @return A boolean indicating result of its evaluation.
   */
  public Boolean evaluate(Map<String, Boolean> context)
      throws UnassignedVariableException;

  /**
   * Simplify the boolean expression.
   * 
   * @param context Map of variable names to its boolean value
   * @return A simplified boolean expression
   */
  public BooleanExpression simplify(Map<String, Boolean> context);

  public boolean equals(Object other);

  public String toString();
}
