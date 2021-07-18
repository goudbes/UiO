package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class Term extends PascalSyntax {
  ArrayList<Factor> factors = new ArrayList<>();
  ArrayList<FactorOperator> factorOperators = new ArrayList<>();
  types.Type type;

  public Term(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<term> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    int i = 0;
    for(Factor factor: factors) {
      factor.prettyPrint();
      try {
        FactorOperator fopr = factorOperators.get(i);
        fopr.prettyPrint();
      } catch (IndexOutOfBoundsException ex) {}
      i++;
    }
  }

  public static Term parse(Scanner s){
    enterParser("term");
    Term term = new Term(s.curLineNum());
    while(true){
      Factor factor = Factor.parse(s);
      factor.context = term;
      term.factors.add(factor);
      if(s.curToken.kind == multiplyToken || s.curToken.kind == divToken ||
         s.curToken.kind == modToken || s.curToken.kind == andToken) {
        FactorOperator fopr = FactorOperator.parse(s);
        fopr.context = term;
        term.factorOperators.add(fopr);
      } else {
        break;
      }
    }
    leaveParser("term");
    return term;
  }

  @Override
  void check(Block curScope, Library lib) {
    for(int i=0; i<factors.size(); i++) {
      Factor f = factors.get(i);
      f.check(curScope, lib);
      if((i-1)<factorOperators.size() && i>0){
        factorOperators.get(i-1).check(curScope, lib);
        f.type.checkType(factors.get(i-1).type, identify(), this, "Type mismatch");
        if(factorOperators.get(i-1).opr == TokenKind.andToken) {
          f.type.checkType(Library.boolType, identify(), this, "and token needs boolean");
        } else {
          f.type.checkType(Library.integerType, identify(), this, "*/div/mod tokens needs integer");
        }
      }
    }
    type = factors.get(0).type;
  }

  @Override
  void genCode(CodeFile f) {
    if(factors.size() > 0) {
      factors.get(0).genCode(f);
      //f.genInstr("", "pushl", "%eax", "");
    }
    for(int i=0; i < factorOperators.size(); i++) {
      f.genInstr("", "pushl", "%eax", "");
      factors.get(i+1).genCode(f);
      FactorOperator opr = factorOperators.get(i);
      if(opr.opr == TokenKind.modToken) {
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "cdq", "", "");
        f.genInstr("", "idivl", "%ecx", "");
        f.genInstr("", "movl", "%edx,%eax", "  mod");
      } else if(opr.opr == TokenKind.divToken) {
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "cdq", "", "");
        f.genInstr("", "idivl", "%ecx", "  /");
      } else if(opr.opr == TokenKind.andToken) {
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "andl", "%ecx,%eax", "  and");
      } else if(opr.opr == TokenKind.multiplyToken) {
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "imull", "%ecx,%eax", "  *");
      } else {
        error(opr.opr + " is not implemented");
      }
    }
  }
}
