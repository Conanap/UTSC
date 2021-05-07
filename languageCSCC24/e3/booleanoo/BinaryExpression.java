package booleanoo;

import java.util.Map;

/**
 * Abstract class for all Binary Expressions.
 */
public abstract class BinaryExpression implements BooleanExpression {
  private BooleanExpression left;
  private BooleanExpression right;
  private BinaryOperator operator;

  /**
   * Constructor.
   * 
   * @param operator What operator do you want
   * @param left Left side of the expression
   * @param right Right side of the expression
   */
  public BinaryExpression(BinaryOperator operator, BooleanExpression left,
      BooleanExpression right) {
    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  /**
   * Evaluate the expression.
   * 
   * @param context A map of variable names to Booleans
   * @return Returns a Boolean that designates what the expression evaluates to
   * @throws UnassignedVariableException If one of the variables is
   *         not in context
   */
  public Boolean evaluate(Map<String, Boolean> context)
      throws UnassignedVariableException {
    if (context == null) {
      throw new UnassignedVariableException();
    }
    return this.operator.apply(this.left.evaluate(context),
        this.right.evaluate(context));
  }

  /**
   * Gives you the left side.
   * 
   * @return The left side of the boolean expression
   */
  protected final BooleanExpression getLeft() {
    return this.left;
  }

  /**
   * Gives you the right side.
   * 
   * @return The right side of the boolean expression
   */
  protected final BooleanExpression getRight() {
    return this.right;
  }

  /**
   * Gives you the operator.
   * 
   * @return The operator of the boolean expression
   */
  protected final BinaryOperator getOperator() {
    return this.operator;
  }

  /**
   * Tests if the other expreesion is equal to this one.
   * 
   * @param other Object to test
   * @return True if equal, false otherwise
   */
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    return this.toString().equals(other.toString()) && other.getClass().equals(this.getClass());
  }

  /**
   * String representation of this object.
   * 
   * @return String representation of this object
   */
  public String toString() {
    return "(" + this.left.toString() + " " + this.operator.toString() + " "
        + this.right.toString() + ")";
  }
}
