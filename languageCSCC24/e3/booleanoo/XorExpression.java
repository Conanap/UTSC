package booleanoo;

import java.util.Map;

/**
 * Xor boolean expressions.
 */
public class XorExpression extends BinaryExpression {

  /**
   * Constructor.
   * 
   * @param left Left side of the expression
   * @param right Right side of the expresison
   */
  public XorExpression(BooleanExpression left, BooleanExpression right) {
    super(new Xor(), left, right);
  }

  /**
   * Simplifies the boolean expression.
   * 
   * @param context Map variables names to boolean values
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
        lbool = null; //chkstyle
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
        rbool = null; // chkstyle
      } // wont happen
    }

    // case 1: both are bool
    if (lbool != null && rbool != null) {
      return new BooleanValue(this.getOperator().apply(lbool, rbool));
    }
    // case 2: r is expression
    if (lbool != null) {
      return lbool ? new Negation(rbe).simplify(context) : rbe;
    }
    // case 3: l is expressions
    // ensure we aren't negating the negation, so simplify is ran
    if (rbool != null) {
      return rbool ? new Negation(lbe).simplify(context) : lbe;
    }
    // both are expressions, can't return this cuz they may be simplified
    BooleanValue alwaysFalse = new BooleanValue(new Boolean(false));
    return lbe.toString().equals(rbe.toString()) ? alwaysFalse
        : new XorExpression(lbe, rbe);
  }
}
