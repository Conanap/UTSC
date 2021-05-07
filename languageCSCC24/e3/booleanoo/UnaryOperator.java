package booleanoo;

/**
 * Interface for all unary operators.
 */
public interface UnaryOperator extends BooleanOperator {
  /**
   * Applies and evaluate expression.
   * 
   * @param operand Operand of the expression
   * @return Evaluated expression
   */
  public Boolean apply(Boolean operand);
}
