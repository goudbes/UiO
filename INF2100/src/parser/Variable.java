package parser;
import main.*;
import scanner.*;
import types.*;

import static scanner.TokenKind.*;

public class Variable extends Factor {

  String name;
  Expression expression;
  PascalDecl pascalDecl;

  public Variable(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<variable> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(name);
    if(expression!=null) {
      Main.log.prettyPrint("[");
      expression.prettyPrint();
      Main.log.prettyPrint("]");
    }
  }

  public static Variable parse(Scanner s) {
    enterParser("variable");
    Variable variable = new Variable(s.curLineNum());
    variable.name = s.curToken.id;
    s.skip(nameToken);

    if (s.curToken.kind == leftBracketToken) {
      s.skip(leftBracketToken);
      variable.expression = Expression.parse(s);
      variable.expression.context = variable;
      s.skip(rightBracketToken);
    }
    leaveParser("variable");
    return variable;
  }

  @Override
  void check(Block curScope, Library lib) {
    pascalDecl = curScope.findDecl(name,this);
    pascalDecl.checkWhetherValue(this);
    type = pascalDecl.type;
    if(expression!=null){
      type.checkType(Library.arrayType, identify(), this, "Variable must be array type");
      expression.check(curScope, lib);
      expression.type.checkType(Library.integerType, identify(), this, "index must be integer");
      type = ((types.ArrayType) type).elemType;
      type.checkType(Library.boolType, identify(), this, "Type mismatch");
    }

  }

  @Override
  void genCode(CodeFile f) {
    if(expression!=null){
      expression.genCode(f);
    }
    if(pascalDecl instanceof ConstDecl) {
      ConstDecl cdecl = (ConstDecl) pascalDecl;
      int val = cdecl.constant.constVal;
      f.genInstr("", "movl", String.format("$%d,%%eax", val), Integer.toString(val));
    } else if (pascalDecl instanceof ParamDecl){
      // Put variable into %eax
      f.genInstr("", "movl", String.format("-%d(%%ebp),%%edx", 4*f.level), "");
      f.genInstr("", "movl", Integer.toString(pascalDecl.declOffset) + "(%edx),%eax", "  " + pascalDecl.name);
    } else if (pascalDecl instanceof VarDecl) {
      if(!(context instanceof AssignStatm)) {
        f.genInstr("", "movl", String.format("-%d(%%ebp),%%edx", 4 * f.level), "");
        // FIXME: 24 is 32-8 since declOffset is hardcoded to start at 8 now
        f.genInstr("", "movl", "-" + (pascalDecl.declOffset + 24) + "(%edx),%eax", "  " + pascalDecl.name);
      }
    }
    else {
      f.genInstr("", "", "", name + " " + pascalDecl.getClass().getName());
    }
  }
}
