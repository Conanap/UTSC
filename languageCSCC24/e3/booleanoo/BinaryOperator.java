package booleanoo;

/**
 * Interface for all binary operators.
 */
public interface BinaryOperator extends BooleanOperator {

  /**
   * Applies and evaluate expression.
   * 
   * @param left Left side of the expression
   * @param right Right side of the expression
   * @return Evaluated expression
   */
  public Boolean apply(Boolean left, Boolean right);
}
