package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Negation extends Factor {
  Factor factor;

  public Negation(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<negation> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" not ");
    factor.prettyPrint();
  }

  public static Negation parse(Scanner s) {
    enterParser("negation");
    s.skip(notToken);
    Negation negation = new Negation(s.curLineNum());
    negation.factor = Factor.parse(s);
    negation.factor.context = negation;
    leaveParser("negation");
    return negation;
  }

  @Override
  void check(Block curScope, Library lib) {
    factor.check(curScope, lib);
    type = factor.type;
    type.checkType(Library.boolType, identify(), this, "Negation must be boolean");
  }

  @Override
  void genCode(CodeFile f) {
    factor.genCode(f);
    f.genInstr("", "xorl", "$1,%eax", "  not");
  }
}
