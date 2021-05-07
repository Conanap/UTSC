package scaler;
// import scaler.LinearScaleFunc;

public class LinearScaler {
  public LinearScaleFunc getScaleFunc(int from, int to) {
    return new LinearScaleFunc(from, to);
  }
}
