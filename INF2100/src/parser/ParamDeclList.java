package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class ParamDeclList extends PascalSyntax {
  ArrayList<ParamDecl> paramDecls = new ArrayList<>();

  public ParamDeclList(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<param decl list> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint("(");
    for(int i=0; i<paramDecls.size(); i++){
      paramDecls.get(i).prettyPrint();
      if(i<paramDecls.size()-1){
        Main.log.prettyPrint(";");
      }
    }
    Main.log.prettyPrint(")");
  }

  public static ParamDeclList parse(Scanner s) {
    enterParser("param decl list");
    ParamDeclList paramDeclList = new ParamDeclList(s.curLineNum());
    s.skip(leftParToken);
    while (true) {
      ParamDecl paramDecl = ParamDecl.parse(s);
      paramDeclList.paramDecls.add(paramDecl);
      if (s.curToken.kind == semicolonToken) {
        s.skip(semicolonToken);
      } else {
        break;
      }
    }
    s.skip(rightParToken);
    leaveParser("param decl list");
    return paramDeclList;
  }

  @Override
  void check(Block curScope, Library lib) {
    for(ParamDecl p: paramDecls) {
      p.check(curScope, lib);
    }
  }

  @Override
  void genCode(CodeFile f) {
    for(int i=0; i<paramDecls.size(); i++) {
      paramDecls.get(i).genCode(f);
      paramDecls.get(i).declOffset += i*4;
    }
  }
}
