package parser;
import main.*;
import scanner.*;
import types.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class ProcCallStatm extends Statement {
  String name;
  ArrayList<Expression> expressions = new ArrayList<>();
  ProcDecl pd;

  public ProcCallStatm(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<proc call> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    //Main.log.prettyPrintLn();
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

  public static ProcCallStatm parse(Scanner s) {
    enterParser("proc call");
    ProcCallStatm procCallStatm = new ProcCallStatm(s.curLineNum());

    s.test(nameToken);
    procCallStatm.name = s.curToken.id;
    s.readNextToken();

    if (s.curToken.kind == leftParToken) {
      s.skip(leftParToken);

      while(true) {
        Expression exp = Expression.parse(s);
        exp.context = procCallStatm;
        procCallStatm.expressions.add(exp);
        if (s.curToken.kind == commaToken) {
          s.skip(commaToken);
        } else {
          break;
        }
      }
      s.skip(rightParToken);
    }
    leaveParser("proc call");
    return procCallStatm;
  }

  @Override
  void check(Block curScope, Library lib) {
    PascalDecl d = curScope.findDecl(name, this);
    d.checkWhetherProcedure(this);
    pd = (ProcDecl) d;
    for (int i = 0; i < expressions.size(); i++) {
      Expression e = expressions.get(i);
      e.check(curScope, lib);
      if(pd.name.equals("write")) {
        e.type.checkNotType(Library.arrayType, identify(), this, "Type mismatch");
      } else {
        e.type.checkType(pd.paramDeclList.paramDecls.get(i).type, identify(), this, "Type mismatch");
      }
    }
  }

  @Override
  void genCode(CodeFile f) {
    if(name.equals("write")) {
      for (Expression e: expressions) {
        e.genCode(f);
        f.genInstr("", "pushl", "%eax", "Push next param.");
        if(e.type == Library.integerType) {
          f.genInstr("", "call", "write_int", "");
        }
        else if(e.type == Library.charType) {
          f.genInstr("", "call", "write_char", "");
        }
        else if(e.type == Library.boolType) {
          f.genInstr("", "call", "write_bool", "");
        }
        else {
          error("Invalid expression type for write");
        }
        f.genInstr("", "addl", "$4,%esp", "Pop param.");
      }
    } else {
      for(int i=expressions.size()-1; i>=0; i--){
        expressions.get(i).genCode(f);
        f.genInstr("", "pushl", "%eax", "Push param #" + (i+1) + ".");
      }
      f.genInstr("", "call", pd.label, "");
      if(!expressions.isEmpty()) {
        f.genInstr("", "addl", String.format("$%d,%%esp", expressions.size() * 4), "Pop params.");
      }
    }
  }
}
