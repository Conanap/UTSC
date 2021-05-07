package booleanoo;

public class UnassignedVariableException extends Exception {

  private static final long serialVersionUID = 1L;

  public UnassignedVariableException() {}

  public UnassignedVariableException(String message) {
    super(message);
  }
}
