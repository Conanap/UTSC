package booleanoo;

import java.util.Map;

/**
 * Negation expression.
 */
public class Negation extends UnaryExpression {

  /**
   * Constructor.
   * 
   * @param operand A boolean expression
   */
  public Negation(BooleanExpression operand) {
    super(new Not(), operand);
  }

  /**
   * Simplifies expression.
   * 
   * @param context Map of variable names to boolean
   * @return A simplified boolean expression
   */
  public BooleanExpression simplify(Map<String, Boolean> context) {
    BooleanExpression be;
    Boolean bool;
    try {
      bool = this.getOperand().evaluate(context);
      return new BooleanValue(new Boolean(!bool));
    } catch (UnassignedVariableException uve) {
      be = this.getOperand().simplify(context);
      if (be.getClass().equals(Negation.class)) {
        return ((Negation) be).getOperand();
      } else if (be.getClass().equals(BooleanValue.class)) {
        // sometimes can't directly evaluate, need to simplify first
        // eg (not (not (a and c))), context = {c = false}
        try {
          bool = be.evaluate(context);
          return new BooleanValue(new Boolean(!bool));
        } catch (UnassignedVariableException uve2) {
          bool = null; // checkstyle complains empty block
        } // wont happen
      }
      // can't return this cuz operand may be simplified
      return new Negation(be);
    }
  }
}
