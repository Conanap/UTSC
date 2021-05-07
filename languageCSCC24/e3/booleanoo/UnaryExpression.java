package booleanoo;

import java.util.Map;

/**
 * Unary expressions.
 */
public abstract class UnaryExpression implements BooleanExpression {
  private UnaryOperator operator;
  private BooleanExpression operand;

  /**
   * Constructor.
   * 
   * @param operator The operator of the expression
   * @param operand The operand of the expression
   */
  public UnaryExpression(UnaryOperator operator, BooleanExpression operand) {
    this.operator = operator;
    this.operand = operand;
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
    return this.operator.apply(this.operand.evaluate(context));
  }

  /**
   * Gives you the operand.
   * 
   * @return The operand of the boolean expression
   */
  protected final BooleanExpression getOperand() {
    return this.operand;
  }

  /**
   * Gives you the operator.
   * 
   * @return The operator of the boolean expression
   */
  protected final UnaryOperator getOperator() {
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
    return "(" + this.operator.toString() + " " + this.operand.toString() + ")";
  }
}
