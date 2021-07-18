package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class EmptyStatm extends Statement {
  public EmptyStatm(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<empty statm> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrintLn(" ");
  }

  public static EmptyStatm parse(Scanner s) {
    enterParser("empty statm");
    EmptyStatm emptyStatm = new EmptyStatm(s.curLineNum());
    //FIXME context Del 3
    leaveParser("empty statm");
    return emptyStatm;
  }

  @Override
  void check(Block curScope, Library lib) {}

  @Override
  void genCode(CodeFile f) {

  }
}
