package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class ConstDeclPart extends PascalSyntax {
  ArrayList<ConstDecl> constDecls = new ArrayList<>();
  Block context = null;

  public ConstDeclPart (int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<const decl part> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrintLn("const ");
    for(ConstDecl decl: constDecls){
      decl.prettyPrint();
    }
  }

  public static ConstDeclPart parse(Scanner s) {
    enterParser("const decl part");
    ConstDeclPart constDeclPart = new ConstDeclPart(s.curLineNum());
    s.skip(constToken);

    while(s.curToken.kind == nameToken) {
      ConstDecl constDecl = ConstDecl.parse(s);
      constDecl.context = constDeclPart;
      constDeclPart.constDecls.add(constDecl);
    }

    leaveParser("const decl part");
    return constDeclPart;
  }

  @Override
  void check(Block curScope, Library lib) {
    for(ConstDecl d: constDecls) {
      d.check(curScope, lib);
    }
  }

  @Override
  void genCode(CodeFile f) {
    for(ConstDecl decl: constDecls) {
      decl.genCode(f);
    }
  }
}
