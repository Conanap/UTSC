package booleanoo;

import java.util.Map;

/**
 * Variables.
 */
public class Variable implements BooleanExpression {
  private String id;

  /**
   * Constructor.
   * 
   * @param id String representaiton of the variable name
   */
  public Variable(String id) {
    this.id = id;
  }

  /**
   * Evaluate the expression, which is just the value.
   * 
   * @param context Map of variable name to boolean values
   * @return a boolean value of the evaluation's result
   * @throws UnassignedVariableException If one of the variables is
   *         not in context
   */
  public Boolean evaluate(Map<String, Boolean> context)
      throws UnassignedVariableException {
    if (context != null && context.containsKey(this.id)) {
      return context.get(this.id);
    }
    throw new UnassignedVariableException();
  }

  /**
   * Simplifies the boolean expression.
   * 
   * @param context Map of variable name to boolean values
   * @return A simplified boolean expression
   */
  public BooleanExpression simplify(Map<String, Boolean> context) {
    if (context != null && context.containsKey(this.id)) {
      return new BooleanValue(new Boolean(context.get(this.id)));
    }
    return this;
  }

  /**
   * Test if the other object is the same as this.
   * 
   * @param other The other object
   * @return Whether they are both the same operator
   */
  public boolean equals(Object other) {
    return this.id == other.toString() && other.getClass().equals(this.getClass());
  }

  /**
   * String representation of the object.
   * 
   * @return String representation of the object
   */
  public String toString() {
    return this.id;
  }
}
