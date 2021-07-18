package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class CompoundStatm extends Statement {
  StatmList statmList;
  public CompoundStatm(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<compund statm> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrintLn("begin");
    Main.log.prettyIndent();
    statmList.prettyPrint();
    Main.log.prettyPrintLn();
    Main.log.prettyOutdent();
    Main.log.prettyPrint("end");
  }

  public static CompoundStatm parse(Scanner s) {
    enterParser("compound statm");
    s.skip(beginToken);
    CompoundStatm compoundStatm = new CompoundStatm(s.curLineNum());
    compoundStatm.statmList = StatmList.parse(s);
    compoundStatm.statmList.context = compoundStatm;
    s.skip(endToken);
    leaveParser("compound statm");
    return compoundStatm;
  }

  @Override
  void check(Block curScope, Library lib) {
    statmList.check(curScope, lib);
  }

  @Override
  void genCode(CodeFile f) {
    statmList.genCode(f);
  }
}
