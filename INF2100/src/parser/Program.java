package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
/* <program> ::= ’program’ <name> ’;’ <block> ’.’ */
public class Program extends PascalDecl {
  Block progBlock;
  Program(String id, int lNum) {
    super(id, lNum);
    progProcFuncName = "prog";
  }

  @Override public String identify() {
     return "<program> " + name + " on line " + lineNum;
  }

  public void prettyPrint() {
    Main.log.prettyPrintLn("program " + name + ";");
    progBlock.prettyPrint();
    Main.log.prettyPrint(".");
  }

  public static Program parse(Scanner s) {
    enterParser("program");
    s.skip(programToken);
    s.test(nameToken);
    Program p = new Program(s.curToken.id, s.curLineNum());
    p.name = s.curToken.id;
    s.readNextToken();
    s.skip(semicolonToken);
    p.progBlock = Block.parse(s);
    p.progBlock.context = p;
    s.skip(dotToken);
    leaveParser("program");
    return p;
  }

  @Override
  void checkWhetherAssignable(PascalSyntax where) {
    where.error("Cannot assign to program");
  }

  @Override
  void checkWhetherFunction(PascalSyntax where) {
    where.error("program is not a function");
  }

  @Override
  void checkWhetherProcedure(PascalSyntax where) {
    where.error("program is not a procedure");
  }

  @Override
  void checkWhetherValue(PascalSyntax where) {
    where.error("program is not a value");
  }

  @Override
  public void check(Block curScope, Library lib) {
    progBlock.check(curScope, lib);
  }

  @Override
  public void genCode(CodeFile f) {
    f.genInstr("", ".globl", "main", "");
    f.genInstr("main", "", "", "");
    String mainLabel = "prog$" + name + "_1";
    f.genInstr("", "call", mainLabel, "Start program");
    f.genInstr("", "movl", "$0,%eax", "Set status 0 and");
    f.genInstr("", "ret", "", "terminate the program");
    progBlock.genCode(f);
    f.genInstr("", "leave", "", "End of " + name);
    f.genInstr("", "ret", "", "");
  }
}
