package booleanoo;

/**
 * If and only if operator.
 */
public class Iff implements BinaryOperator {

  /**
   * String representation of the object.
   * 
   * @return String representation of the object
   */
  public String toString() {
    return Constants.IFF;
  }

  /**
   * Test whether the other object is the same.
   * 
   * @param other Any object to test
   * @return Whether or not object are equal
   */
  public boolean equals(Object other) {
    return other.getClass().equals(Iff.class);
  }

  /**
   * Apply operator to the two sides of the operator.
   * 
   * @param left Left side of the expression
   * @param right Right side of the expression
   * @return Result of the evaluation
   */
  public Boolean apply(Boolean left, Boolean right) {
    return new Boolean((left && right) || (!left && !right));
  }
}
