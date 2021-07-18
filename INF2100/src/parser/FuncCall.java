package parser;
import main.*;
import scanner.*;
import types.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class FuncCall extends Factor {
  String name;
  ArrayList<Expression> expressions = new ArrayList<>();
  PascalSyntax context;
  FuncDecl fd;

  public FuncCall(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<func call> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(name);
    if(!expressions.isEmpty()) {
      Main.log.prettyPrint("(");
    }

    for(int i=0; i<expressions.size(); i++){
      expressions.get(i).prettyPrint();
      if(i<expressions.size()-1){
        Main.log.prettyPrint(", ");
      }
    }

    if(!expressions.isEmpty()) {
      Main.log.prettyPrint(")");
    }
  }

  public static FuncCall parse(Scanner s) {
    enterParser("func call");
    FuncCall funcCall = new FuncCall(s.curLineNum());
    funcCall.name = s.curToken.id;
    s.skip(nameToken);
    if (s.curToken.kind == leftParToken) {
      s.skip(leftParToken);
      while(true) {
        Expression exp = Expression.parse(s);
        exp.context = funcCall;
        funcCall.expressions.add(exp);
        if(s.curToken.kind == commaToken) {
          s.skip(commaToken);
        } else {
          break;
        }
      }
      s.skip(rightParToken);
    }
    leaveParser("func call");
    return funcCall;
  }

  @Override
  void check(Block curScope, Library lib) {
    PascalDecl d = curScope.findDecl(name, this);
    d.checkWhetherFunction(this);
    fd = (FuncDecl) d;
    type = fd.type;
    for(int c=0; c<expressions.size(); c++){
      Expression e = expressions.get(c);
      e.check(curScope, lib);
      e.type.checkType(fd.paramDeclList.paramDecls.get(c).type, identify(), this, "Type mismatch");
    }
  }

  @Override
  void genCode(CodeFile f) {
    for(int i=expressions.size()-1; i>=0; i--){
      expressions.get(i).genCode(f);
      f.genInstr("", "pushl", "%eax", "Push param #" + (i+1));
    }
    f.genInstr("", "call", fd.label, "");
    f.genInstr("", "addl", String.format("$%d,%%esp", expressions.size()*4), "Pop parameters");
  }
}
