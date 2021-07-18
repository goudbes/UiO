package parser;

import main.*;

public abstract class PascalSyntax {
  public int lineNum;

  // FIXME: Is is needed everywhere, del 3
  public PascalSyntax context;

  PascalSyntax(int n) {
    lineNum = n;
  }

  abstract void check(Block curScope, Library lib);
  abstract void genCode(CodeFile f);
  abstract public String identify();
  abstract void prettyPrint();

  public void error(String message) {
    Main.error("Error at line " + lineNum + ": " + message);
  }

  static void enterParser(String nonTerm) {
    Main.log.enterParser(nonTerm);
  }

  static void leaveParser(String nonTerm) {
    Main.log.leaveParser(nonTerm);
  }
}
