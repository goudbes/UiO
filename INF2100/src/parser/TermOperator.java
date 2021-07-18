package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TermOperator extends Operator {
  TokenKind opr;

  public TermOperator(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<term opr> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" " + opr.toString() + " ");
  }

  public static TermOperator parse(Scanner s){
    enterParser("term opr");
    TermOperator termOperator = new TermOperator(s.curLineNum());
    switch(s.curToken.kind){
      case addToken:
      case subtractToken:
      case orToken:
      termOperator.opr = s.curToken.kind;
      s.skip(s.curToken.kind);
      break;
      default:
      s.testError(s.curToken.kind.toString());
      break;
    }
    leaveParser("term opr");
    return termOperator;
  }

  @Override
  void check(Block curScope, Library lib) {}

  @Override
  void genCode(CodeFile f) {

  }
}
