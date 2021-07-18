package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class SimplExpr extends Expression {
  PrefixOperator prefixOperator;
  ArrayList<Term> terms = new ArrayList<>();
  ArrayList<TermOperator> termOperators = new ArrayList<>();

  public SimplExpr(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<simple expr> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    if(prefixOperator!=null){
      prefixOperator.prettyPrint();
    }
    for(int i=0; i<terms.size(); i++){
      terms.get(i).prettyPrint();
      if(i<terms.size()-1){
        termOperators.get(i).prettyPrint();
      }
    }
  }

  public static SimplExpr parse(Scanner s){
    enterParser("simple expr");
    SimplExpr simplExpr = new SimplExpr(s.curLineNum());
    if(s.curToken.kind == addToken || s.curToken.kind == subtractToken) {
      simplExpr.prefixOperator = PrefixOperator.parse(s);
      simplExpr.prefixOperator.context = simplExpr;
    }
    while(true){
      Term term = Term.parse(s);
      term.context = simplExpr;
      simplExpr.terms.add(term);
      if(s.curToken.kind != addToken && s.curToken.kind != subtractToken && s.curToken.kind != orToken ) {
        break;
      }
      TermOperator opr = TermOperator.parse(s);
      opr.context = simplExpr;
      simplExpr.termOperators.add(opr);
    }
    leaveParser("simple expr");
    return simplExpr;
  }

  @Override
  void check(Block curScope, Library lib) {
    for(Term t: terms) {
      t.check(curScope, lib);
    }
    if(prefixOperator!=null) {
      prefixOperator.check(curScope, lib);
      terms.get(0).type.checkType(Library.integerType, identify(), this, "Prefix operator must precede integer");
    }
    for(int i=0; i<termOperators.size(); i++){
      TermOperator opr = termOperators.get(i);
      if(opr.opr == TokenKind.orToken) {
        terms.get(i).type.checkType(Library.boolType, identify(), this, "or requires boolean");
      } else {
        terms.get(i).type.checkType(Library.integerType, identify(), this, "+/- requires integer");
        terms.get(i+1).type.checkType(Library.integerType, identify(), this, "+/- requires integer");
      }
    }
    type = terms.get(0).type;
  }

  @Override
  void genCode(CodeFile f) {
    if(terms.size() > 0) {
      terms.get(0).genCode(f);
    }
    for(int i=0; i<termOperators.size(); i++){
      f.genInstr("", "pushl", "%eax", "");
      TermOperator opr = termOperators.get(i);
      terms.get(i+1).genCode(f);
      if(opr.opr == TokenKind.addToken) {
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "addl", "%ecx,%eax", "  +");
      } else if(opr.opr == TokenKind.subtractToken){
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "subl", "%ecx,%eax", "  -");
      } else if(opr.opr == TokenKind.andToken){
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "andl", "%ecx,%eax", "  and");
      } else if(opr.opr == TokenKind.orToken) {
        f.genInstr("", "movl", "%eax,%ecx", "");
        f.genInstr("", "popl", "%eax", "");
        f.genInstr("", "orl", "%ecx,%eax", "  or");
      } else {
        error(opr.opr + " not implemented");
      }
    }
    if(prefixOperator!=null) {
      prefixOperator.genCode(f);
    }
  }
}
