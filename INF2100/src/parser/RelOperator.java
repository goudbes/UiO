package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class RelOperator extends Operator {
  TokenKind opr;

  public RelOperator(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<rel opr> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" " + opr.toString() + " ");
  }

  public static RelOperator parse(Scanner s){
    enterParser("rel opr");
    RelOperator relOperator = new RelOperator(s.curLineNum());
    switch(s.curToken.kind){
      case equalToken:
      case notEqualToken:
      case lessToken:
      case lessEqualToken:
      case greaterToken:
      case greaterEqualToken:
      relOperator.opr = s.curToken.kind;
      s.skip(s.curToken.kind);
      break;
      default:
      s.testError(s.curToken.kind.toString());
      break;
    }
    leaveParser("rel opr");
    return relOperator;
  }

  @Override
  void check(Block curScope, Library lib) {

  }

  @Override
  void genCode(CodeFile f) {

  }
}
