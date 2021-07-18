package parser;
import main.*;
import scanner.*;
import types.BoolType;

import static scanner.TokenKind.*;

public class IfStatm extends Statement {
  Expression expression;
  Statement mainStatement;
  Statement elseStatement;

  public IfStatm(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<if statm> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint("if ");
    expression.prettyPrint();
    Main.log.prettyPrintLn(" then ");
    Main.log.prettyIndent();
    mainStatement.prettyPrint();
    Main.log.prettyOutdent();
    if(elseStatement!=null){
      Main.log.prettyPrintLn();
      Main.log.prettyPrintLn("else ");
      Main.log.prettyIndent();
      elseStatement.prettyPrint();
      Main.log.prettyOutdent();
    }
  }

  public static IfStatm parse(Scanner s){
    enterParser("if-statm");
    s.skip(ifToken);
    IfStatm ifStatm = new IfStatm(s.curLineNum());
    ifStatm.expression = Expression.parse(s);
    ifStatm.expression.context = ifStatm;
    s.skip(thenToken);
    ifStatm.mainStatement = Statement.parse(s);
    ifStatm.mainStatement.context = ifStatm;
    if(s.curToken.kind == elseToken) {
      s.skip(elseToken);
      ifStatm.elseStatement = Statement.parse(s);
      ifStatm.elseStatement.context = ifStatm;
    }
    leaveParser("if-statm");
    return ifStatm;
  }

  @Override
  void check(Block curScope, Library lib) {
    expression.check(curScope, lib);
    mainStatement.check(curScope, lib);
    if(elseStatement!=null){
      elseStatement.check(curScope, lib);
    }
    expression.type.checkType(Library.boolType, identify(), this, "If expression must be boolean");
  }

  @Override
  void genCode(CodeFile f) {
    f.genInstr("", "", "", "Start if-statement");
    expression.genCode(f);
    f.genInstr("", "cmpl", "$0,%eax", "");
    String lab1 = f.getLocalLabel();
    f.genInstr("", "je", lab1, "");
    mainStatement.genCode(f);
    String lab2 = f.getLocalLabel();
    f.genInstr("", "jmp", lab2, "");
    f.genInstr(lab1, "", "", "");
    if(elseStatement!=null){
      elseStatement.genCode(f);
    }
    f.genInstr(lab2, "", "", "");
    f.genInstr("", "", "", "End if-statement");
  }
}
