package booleanoo;

/**
 * Class for the AND operator.
 */
public class And implements BinaryOperator {
  /**
   * Constructor.
   */
  public String toString() {
    return Constants.AND;
  }

  /**
   * Testing if it's equal, test by whether they are both AND operators.
   * 
   * @return Whether it's equal
   */
  public boolean equals(Object other) {
    return other.getClass().equals(And.class);
  }

  /**
   * Apply left AND right.
   * 
   * @return Outcome of evaluting left AND right
   */
  public Boolean apply(Boolean left, Boolean right) {
    return new Boolean(left && right);
  }
}
