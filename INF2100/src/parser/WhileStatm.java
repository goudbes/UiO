package parser;
import main.*;
import scanner.*;
import types.BoolType;

import static scanner.TokenKind.*;

public class WhileStatm extends Statement {
  Expression expression;
  Statement statement;
  public WhileStatm(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<while-statm> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    //Main.log.prettyPrintLn();
    Main.log.prettyPrint("while ");
    expression.prettyPrint();
    Main.log.prettyPrintLn(" do");
    Main.log.prettyIndent();
    statement.prettyPrint();
    Main.log.prettyOutdent();
  }

  public static WhileStatm parse(Scanner s) {
    enterParser("while-statm");
    WhileStatm whileStatm = new WhileStatm(s.curLineNum());
    s.skip(whileToken);
    whileStatm.expression = Expression.parse(s);
    whileStatm.expression.context = whileStatm;
    s.skip(doToken);
    whileStatm.statement = Statement.parse(s);
    whileStatm.statement.context = whileStatm;
    leaveParser("while-statm");
    return whileStatm;
  }

  @Override
  void check(Block curScope, Library lib) {
    expression.check(curScope, lib);
    statement.check(curScope, lib);
    expression.type.checkType(Library.boolType, identify(), this, "Type mismatch");
  }

  @Override
  void genCode(CodeFile f) {
    String testLabel = f.getLocalLabel();
    String endLabel = f.getLocalLabel();
    f.genInstr(testLabel,"","","Start while-statement");
    expression.genCode(f);
    f.genInstr("", "cmpl", "$0,%eax", "");
    f.genInstr("", "je", endLabel, "");
    statement.genCode(f);
    f.genInstr("", "jmp", testLabel, "");
    f.genInstr(endLabel, "", "", "End while-statement");
  }
}
