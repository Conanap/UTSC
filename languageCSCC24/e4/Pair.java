package poly;

public class Pair<T1, T2> {
  private T1 first;
  private T2 second;

  /**
  * Constructor.
  * @param first The first element in the pair
  * @param second The second element in the pair
  */
  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  /**
  * Get the first element.
  * @return The first element in the pair
  */
  public T1 getFirst() {
    return this.first;
  }

  /**
  * Get the second element.
  * @return The second element in the pair
  */
  public T2 getSecond() {
    return this.second;
  }

  /**
  * String representation of the pair.
  * @return String representation of the pair
  */
  public String toString() {
    return "(" + this.first.toString() + "," + this.second.toString() + ")";
  }
}
