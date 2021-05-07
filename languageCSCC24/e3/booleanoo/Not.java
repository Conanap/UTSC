package booleanoo;

/**
 * Not operator.
 */
public class Not implements UnaryOperator {

  /**
   * String representation of the object.
   * 
   * @return String representation of the object
   */
  public String toString() {
    return Constants.NOT;
  }

  /**
   * Test if the other object is the same as this.
   * 
   * @param other The other object
   * @return Whether they are both the same operator
   */
  public boolean equals(Object other) {
    return other.getClass().equals(Not.class);
  }

  /**
   * Applies and evaluate expression.
   * 
   * @param operand Expression to evaluate
   * @return Evaluated expression
   */
  public Boolean apply(Boolean operand) {
    return new Boolean(!operand);
  }
}
