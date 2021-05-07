package booleanoo;

/**
 * Boolean operators.
 */
public interface BooleanOperator {

  /**
   * String representation of the operator.
   * 
   * @return String representation of the object
   */
  public String toString();

  /**
   * Tests if another object is equal.
   * 
   * @return Return whether the other object is equal
   */
  public boolean equals(Object other);
}
