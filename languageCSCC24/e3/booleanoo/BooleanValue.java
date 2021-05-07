package booleanoo;

import java.util.Map;

/**
 * Class for boolean values.
 */
public class BooleanValue implements BooleanExpression {
  private Boolean value;

  /**
   * Constructor.
   * 
   * @param val A Boolean
   */
  public BooleanValue(Boolean val) {
    this.value = val;
  }

  /**
   * Evaluate the expression, which is just the value.
   * 
   * @param context Map of variable name to boolean values
   * @return a boolean value of the evaluation's result
   * @throws booleanoo.UnassignedVariableException If one of the variables is
   *         not in context
   */
  public Boolean evaluate(Map<String, Boolean> context) {
    return this.value;
  }

  /**
   * Simplifies the boolean expression.
   * 
   * @param context Map of variable name to boolean values
   * @return A simplified boolean expression
   */
  public BooleanExpression simplify(Map<String, Boolean> context) {
    return this;
  }

  /**
   * Test if the other object is the same as this.
   * 
   * @param other The other object
   * @return Whether they are both the same operator
   */
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other.getClass().getSuperclass().equals(BooleanValue.class)) {
      return this.evaluate(null) == ((BooleanValue) other).evaluate(null);
    }
    return false;
  }

  /**
   * String representation of the object.
   * 
   * @return String representation of the object
   */
  public String toString() {
    return this.value.toString();
  }
}
