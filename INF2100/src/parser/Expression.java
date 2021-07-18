package parser;
import main.*;
import scanner.*;
import types.BoolType;

import static scanner.TokenKind.*;

public class Expression extends PascalSyntax {
  SimplExpr simplExpr;
  SimplExpr maybeSimplExpr;
  RelOperator relOperator;
  types.Type type;

  public Expression(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<expression> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    simplExpr.prettyPrint();
    if(maybeSimplExpr!=null){
      relOperator.prettyPrint();
      maybeSimplExpr.prettyPrint();
    }
  }

  public static Expression parse(Scanner s){
    enterParser("expression");
    Expression expression = new Expression(s.curLineNum());
    expression.simplExpr = SimplExpr.parse(s);
    expression.simplExpr.context = expression;
    switch(s.curToken.kind) {
      case equalToken:
      case notEqualToken:
      case lessToken:
      case lessEqualToken:
      case greaterToken:
      case greaterEqualToken:
      expression.relOperator = RelOperator.parse(s);
      expression.relOperator.context = expression;
      expression.maybeSimplExpr = SimplExpr.parse(s);
      break;
      default:
      break;
    }
    leaveParser("expression");
    return expression;
  }

  @Override
  void check(Block curScope, Library lib) {
    simplExpr.check(curScope, lib);
    type = simplExpr.type;
    if(relOperator!=null){
      type = Library.boolType;
      relOperator.check(curScope, lib);
      maybeSimplExpr.check(curScope, lib);
      simplExpr.type.checkType(maybeSimplExpr.type, identify(), this, "Type mismatch");
      type.checkType(Library.boolType, identify(), this, "Type mismatch");
    }
  }

  @Override
  void genCode(CodeFile f) {
    simplExpr.genCode(f);

    if(relOperator!=null){
      relOperator.genCode(f);
      f.genInstr("", "pushl", "%eax", "");
      maybeSimplExpr.genCode(f);
      f.genInstr("", "popl", "%ecx", "");
      f.genInstr("", "cmpl", "%eax,%ecx", "");
      f.genInstr("", "movl", "$0,%eax", "");
      if(relOperator.opr == TokenKind.equalToken) {
        f.genInstr("", "sete", "%al", "Test =");
      } else if(relOperator.opr == TokenKind.lessToken) {
        f.genInstr("", "setl", "%al", "Test <");
      } else if(relOperator.opr == TokenKind.greaterToken) {
        f.genInstr("", "setg", "%al", "Test >");
      }else if(relOperator.opr == TokenKind.greaterEqualToken) {
        f.genInstr("", "setge", "%al", "Test >=");
      }else if(relOperator.opr == TokenKind.lessEqualToken) {
        f.genInstr("", "setle", "%al", "Test <=");
      }else if(relOperator.opr == TokenKind.notEqualToken) {
        f.genInstr("", "setne", "%al", "Test <>");
      } else {
        error(relOperator.opr + " not implemented");
      }
    }
  }
}
