package parser;
import main.*;
import scanner.*;

import java.util.ArrayList;

import static scanner.TokenKind.*;

public class StatmList extends PascalSyntax {
  ArrayList<Statement> statements = new ArrayList<>();

  public StatmList(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<statm list> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyIndent();
    for(int i=0; i<statements.size(); i++){
      if (i > 0) {
          Main.log.prettyPrintLn();
      }
      statements.get(i).prettyPrint();
      if(i<statements.size()-1){
        Main.log.prettyPrint(";");
      }
    }
    Main.log.prettyOutdent();
  }

  public static StatmList parse(Scanner s) {
    enterParser("statm list");
    StatmList statmList = new StatmList(s.curLineNum());
    while (true) {
      statmList.statements.add(Statement.parse(s));
      if (s.curToken.kind == semicolonToken) {
        s.skip(semicolonToken);
      } else {
        break;
      }
    }
    leaveParser("statm list");
    return statmList;
  }

  @Override
  void check(Block curScope, Library lib) {
    for(Statement s: statements) {
      s.check(curScope, lib);
    }
  }

  @Override
  void genCode(CodeFile f) {
    for(Statement s: statements) {
      s.genCode(f);
    }
  }
}
