package booleanoo;

import java.util.Map;

/**
 * Implication expression.
 */
public class Implication extends BinaryExpression {
  public Implication(BooleanExpression left, BooleanExpression right) {
    super(new Implies(), left, right);
  }

  /**
   * Simplifies the expression.
   * 
   * @param context Map of variable names to boolean
   * @return Simplified expression
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
      // sometimes can't directly evaluate, need to simplify first
      // eg (not (not (a and c))), context = {c = false}
      try {
        if (rbe.getClass().equals(BooleanValue.class)) {
          rbool = rbe.evaluate(context);
        }
      } catch (UnassignedVariableException uve2) {
        rbool = null; // checkstyle complains empty block
      } // wont happen
    }

    // chkstyle wants it here instead
    BooleanValue alwaysTrue = new BooleanValue(new Boolean(true));
    // case 1: both are bool
    if (lbool != null && rbool != null) {
      return new BooleanValue(this.getOperator().apply(lbool, rbool));
    }
    // case 2: r is expression
    if (lbool != null) {
      return lbool ? rbe : alwaysTrue;
    }
    // case 3: l is expressions
    // ensure we aren't negating the negation, so simplify is ran
    if (rbool != null) {
      return rbool ? alwaysTrue : new Negation(lbe).simplify(context);
    }
    // both are expressions, can't return this cuz they may be simplified
    return lbe.toString().equals(rbe.toString()) ? alwaysTrue
        : new Implication(lbe, rbe);
  }
}
