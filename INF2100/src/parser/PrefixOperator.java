package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class PrefixOperator  extends Operator {

  public PrefixOperator(int lNum) {
    super(lNum);
  }
  TokenKind opr;

  @Override
  public String identify() {
    return "<prefix opr> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" " + opr.toString() +" ");
  }

  public static PrefixOperator parse(Scanner s) {
    enterParser("prefix opr");
    PrefixOperator prefixOperator = new PrefixOperator(s.curLineNum());
    prefixOperator.opr = s.curToken.kind;
    if (s.curToken.kind == addToken) {
      s.skip(addToken);
    } else if (s.curToken.kind == subtractToken) {
      s.skip(subtractToken);
    } else {
      s.testError(s.curToken.kind.toString());
    }

    leaveParser("prefix opr");
    return prefixOperator;
  }

  @Override
  void check(Block curScope, Library lib) {}

  @Override
  void genCode(CodeFile f) {
    if(opr == subtractToken) {
      f.genInstr("", "negl", "%eax", "  - (prefix)");
    }
  }
}
