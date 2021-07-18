package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class VarDeclPart extends PascalSyntax {
  ArrayList<VarDecl> varDeclParts = new ArrayList<>();

  public VarDeclPart (int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<var decl part> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrintLn();
    Main.log.prettyPrintLn("var ");
    for(VarDecl decl: varDeclParts) {
      decl.prettyPrint();
    }
  }

  public static VarDeclPart parse(Scanner s) {
    enterParser("var decl part");
    VarDeclPart varDeclPart = new VarDeclPart(s.curLineNum());
    s.skip(varToken);

    while(true) {
      VarDecl varDecl = VarDecl.parse(s);
      varDecl.context = varDeclPart;
      varDeclPart.varDeclParts.add(varDecl);
      if(s.curToken.kind != nameToken){
        break;
      }
    }

    leaveParser("var decl part");
    return varDeclPart;
  }

  @Override
  void check(Block curScope, Library lib) {
    for(VarDecl v: varDeclParts) {
      v.check(curScope, lib);
    }
  }

  @Override
  void genCode(CodeFile f) {
    for(int i=0; i<varDeclParts.size(); i++) {
      varDeclParts.get(i).genCode(f);
      varDeclParts.get(i).declOffset += (i+1)*4;
    }
  }
}
