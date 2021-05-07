package booleanoo;

/**
 * Implies operator.
 */
public class Implies implements BinaryOperator {

  /**
   * String representation of object.
   * 
   * @return String representation of the object
   */
  public String toString() {
    return Constants.IMPLIES;
  }

  /**
   * Test if the other object is the same as this.
   * 
   * @param other The other object
   * @return Whether they are both the same operator
   */
  public boolean equals(Object other) {
    return other.getClass().equals(Implies.class);
  }

  /**
   * Applies and evaluate expression.
   * 
   * @param left Left side of the expression
   * @param right Right side of the expression
   * @return Evaluated expression
   */
  public Boolean apply(Boolean left, Boolean right) {
    return new Boolean(right || ((!left) && (!right)));
  }
}
