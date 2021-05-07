package scaler;

public class LinearScaleFunc {
  private int to, from;

  public LinearScaleFunc(int from, int to) {
    this.to = to;
    this.from = from;
  }

  public double scale(double value) {
    return (value * to) / from;
  }
}
