package parser;
import main.*;
import scanner.*;
import types.*;

import static scanner.TokenKind.*;

public class AssignStatm extends Statement {
  Variable variable;
  Expression expression;

  public AssignStatm(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<assign statm> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    variable.prettyPrint();
    Main.log.prettyPrint(" := ");
    expression.prettyPrint();
  }

  public static AssignStatm parse(Scanner s) {
    enterParser("assign statm");
    AssignStatm assignStatm = new AssignStatm(s.curLineNum());
    //variable
    assignStatm.variable = Variable.parse(s);
    assignStatm.variable.context = assignStatm;
    //assignToken
    s.skip(assignToken);
    assignStatm.expression = Expression.parse(s);
    assignStatm.expression.context = assignStatm;

    leaveParser("assign statm");
    return assignStatm;
  }

  @Override
  void check(Block curScope, Library lib) {
    variable.check(curScope, lib);
    variable.pascalDecl.checkWhetherAssignable(this);
    expression.check(curScope, lib);
    if((variable.type instanceof types.ArrayType)) {
      if(variable.expression == null) {
        error("Cannot assign to array");
      } else {
        ((types.ArrayType)variable.type).elemType.checkType(expression.type, this.identify(), this,
                "Type mismatch " + variable.expression.type.identify() + " != " + expression.type.identify());
      }
    } else {
      variable.type.checkType(expression.type, this.identify(), this, "Type mismatch");
    }
  }

  @Override
  void genCode(CodeFile f) {
    variable.genCode(f);
    expression.genCode(f);
    if(variable.pascalDecl instanceof FuncDecl) {
      f.genInstr("", "movl", String.format("-%d(%%ebp),%%edx", 4*f.level), "");
      f.genInstr("", "movl", "%eax,-32(%edx)", "");
    }
    if(variable.pascalDecl instanceof VarDecl) {
      f.genInstr("", "movl", String.format("-%d(%%ebp),%%edx", 4*f.level), "");
      f.genInstr("", "movl", String.format("%%eax,-%d(%%edx)",
              variable.pascalDecl.declOffset + 24), variable.name + " :=");
    }
  }
}
