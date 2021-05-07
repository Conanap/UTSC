package booleanoo;

import java.util.Map;

/**
 * Disjunction boolean expression.
 */
public class Disjunction extends BinaryExpression {

  /**
   * Constructor.
   * 
   * @param left Left side of the expression
   * @param right Right side of the expression
   */
  public Disjunction(BooleanExpression left, BooleanExpression right) {
    super(new Or(), left, right);
  }

  /**
   * Simplifies the expression.
   * 
   * @param context Map of variable names to boolean values
   * @return A simplified boolean expression
   */
  public BooleanExpression simplify(Map<String, Boolean> context) {
    BooleanExpression lbe = null;
    BooleanExpression rbe = null;
    Boolean lbool = null;
    Boolean rbool = null;

    try {
      lbool = this.getLeft().evaluate(context);
    } catch (UnassignedVariableException uve) {
      lbe = this.getLeft().simplify(context);
      // sometimes can't directly evaluate, need to simplify first
      // eg (not (not (a and c))), context = {c = false}
      try {
        if (lbe.getClass().equals(BooleanValue.class)) {
          lbool = lbe.evaluate(context);
        }
      } catch (UnassignedVariableException uve2) {
        lbool = null; // checkstyle complains empty block
      } // wont happen
    }
    try {
      rbool = this.getRight().evaluate(context);
    } catch (UnassignedVariableException uve) {
      rbe = this.getRight().simplify(context);
      try {
        if (rbe.getClass().equals(BooleanValue.class)) {
          rbool = rbe.evaluate(context);
        }
      } catch (UnassignedVariableException uve2) {
        rbool = null; // checkstyle complains empty block
      } // wont happen
    }

    // case 1: both are bool
    if (lbool != null && rbool != null) {
      return new BooleanValue(new Boolean(lbool || rbool));
    }
    // case 2: r is expression
    if (lbool != null) {
      return rbe;
    }
    // case 3: l is expressions
    if (rbool != null) {
      return lbe;
    }
    // both are expressions, can't return this cuz they may be simplified
    return lbe.toString().equals(rbe.toString()) ? lbe
        : new Disjunction(lbe, rbe);
  }
}
