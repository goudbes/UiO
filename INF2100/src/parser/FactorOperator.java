package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class FactorOperator extends Operator {
  TokenKind opr;

  public FactorOperator(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<factor opr> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" " + opr.toString() + " ");
  }

  public static FactorOperator parse(Scanner s){
    enterParser("factor opr");
    FactorOperator factorOperator = new FactorOperator(s.curLineNum());
    switch(s.curToken.kind){
      case multiplyToken:
      case divToken:
      case modToken:
      case andToken:
        factorOperator.opr = s.curToken.kind;
        s.skip(s.curToken.kind);
      break;
      default:
      s.testError("(*, div, mod, and)");
    }
    leaveParser("factor opr");
    return factorOperator;
  }

  @Override
  void check(Block curScope, Library lib) {

  }

  @Override
  void genCode(CodeFile f) {

  }
}
