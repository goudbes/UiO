package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ProcDecl extends PascalDecl {
  ParamDeclList paramDeclList;
  Block block;
  PascalSyntax context;

  public ProcDecl(String id,int lNum) {
    super(id,lNum);
    progProcFuncName = "proc";
  }

  @Override
  public String identify() {
    return "<proc decl> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrintLn();
    Main.log.prettyPrintLn();
    Main.log.prettyPrint("procedure ");
    Main.log.prettyPrint(name);
    if(paramDeclList!=null) {
      paramDeclList.prettyPrint();
    }
    Main.log.prettyPrint(";");
    block.prettyPrint();
    Main.log.prettyPrint(";");
  }

  public static ProcDecl parse(Scanner s) {
    enterParser("proc decl");
    ProcDecl procDecl = new ProcDecl(s.curToken.id,s.curLineNum());
    //function token
    s.skip(procedureToken);
    //name
    procDecl.name = s.curToken.id;
    s.skip(nameToken);
    //param decl list if it's there
    if (s.curToken.kind == leftParToken) {
      procDecl.paramDeclList = ParamDeclList.parse(s);
      procDecl.paramDeclList.context = procDecl;
    }
    s.skip(semicolonToken);
    //Block
    procDecl.block = Block.parse(s);
    procDecl.block.context = procDecl;
    s.skip(semicolonToken);
    leaveParser("proc decl");
    return procDecl;
  }

  @Override
  void checkWhetherAssignable(PascalSyntax where) {
    where.error("Cannot assign to proc");
  }

  @Override
  void checkWhetherFunction(PascalSyntax where) {
    where.error("proc is not a function");
  }

  @Override
  void checkWhetherProcedure(PascalSyntax where) {}

  @Override
  void checkWhetherValue(PascalSyntax where) {
    where.error("proc is not a value");
  }

  @Override
  void check(Block curScope, Library lib) {
    curScope.addDecl(name, this);
    block.outerScope = curScope;

    if(paramDeclList!=null) {
      paramDeclList.check(block, lib);
    }
    block.check(curScope, lib);
  }

  @Override
  void genCode(CodeFile f) {
    declLevel = f.level+1;
    if(paramDeclList!=null) {
      paramDeclList.genCode(f);
    }
    block.genCode(f);
    f.genInstr("", "leave", "", "End of " + name);
    f.genInstr("", "ret", "", "");
  }
}
