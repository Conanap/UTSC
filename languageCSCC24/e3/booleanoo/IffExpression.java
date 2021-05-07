package booleanoo;

import java.util.Map;

/**
 * If and only if expression.
 */
public class IffExpression extends BinaryExpression {
  public IffExpression(BooleanExpression left, BooleanExpression right) {
    super(new Iff(), left, right);
  }

  /**
   * Simplifies the expression.
   * 
   * @param context Map of variable names to boolean
   * @return A simplified expression
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

    // case 1: both are bool
    if (lbool != null && rbool != null) {
      return new BooleanValue(this.getOperator().apply(lbool, rbool));
    }
    // case 2: r is expression
    if (lbool != null) {
      return lbool ? rbe : new Negation(rbe).simplify(context);
    }
    // case 3: l is expressions
    if (rbool != null) { // negating the negation, so simplify is ran
      return rbool ? lbe : new Negation(lbe).simplify(context);
    }
    // both are expressions, can't return this cuz they may be simplified
    BooleanValue alwaysTrue = new BooleanValue(new Boolean(true)); // chckstyle again
    return lbe.toString().equals(rbe.toString()) ? alwaysTrue
        : new IffExpression(lbe, rbe);
  }
}
