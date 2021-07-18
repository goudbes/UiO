package parser;
import main.*;
import scanner.*;
import types.*;

import static scanner.TokenKind.*;

public class InnerExpr extends Factor {
  Expression expression;

  public InnerExpr(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<inner expr> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint("(");
    expression.prettyPrint();
    Main.log.prettyPrint(")");
  }

  public static InnerExpr parse(Scanner s) {
    enterParser("inner expr");
    InnerExpr innerExpr = new InnerExpr(s.curLineNum());
    s.skip(leftParToken);
    innerExpr.expression = Expression.parse(s);
    innerExpr.expression.context = innerExpr;
    s.skip(rightParToken);
    leaveParser("inner expr");
    return innerExpr;
  }

  @Override
  void check(Block curScope, Library lib) {
    expression.check(curScope, lib);
    type = expression.type;
  }

  @Override
  void genCode(CodeFile f) {
    expression.genCode(f);
  }
}
