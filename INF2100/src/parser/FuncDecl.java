package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class FuncDecl extends ProcDecl {
  TypeName typeName;
  ParamDeclList paramDeclList;
  Block block;
  //String label;

  public FuncDecl(String id,int lNum) {
    super(id,lNum);
    progProcFuncName = "func";
  }

  @Override
  public String identify() {
    return "<func decl> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrintLn();
    Main.log.prettyPrint("function ");
    Main.log.prettyPrint(name);
    if(paramDeclList!=null) {
      paramDeclList.prettyPrint();
    }
    Main.log.prettyPrint(":");
    typeName.prettyPrint();
    Main.log.prettyPrint(";");
    block.prettyPrint();
    Main.log.prettyPrint(";");
    Main.log.prettyPrintLn();
  }

  public static FuncDecl parse(Scanner s) {
    enterParser("func decl");
    FuncDecl funcDecl = new FuncDecl(s.curToken.id,s.curLineNum());
    //function token
    s.skip(functionToken);
    //name
    funcDecl.name = s.curToken.id;
    s.skip(nameToken);
    //param decl list if it's there
    if (s.curToken.kind == leftParToken) {
      funcDecl.paramDeclList = ParamDeclList.parse(s);
      funcDecl.paramDeclList.context = funcDecl;
    }
    s.skip(colonToken);
    //type name
    funcDecl.typeName = TypeName.parse(s);
    funcDecl.typeName.context = funcDecl;
    s.skip(semicolonToken);
    //Block
    funcDecl.block = Block.parse(s);
    funcDecl.block.context = funcDecl;
    s.skip(semicolonToken);
    leaveParser("func decl");
    return funcDecl;
  }

  @Override
  void checkWhetherAssignable(PascalSyntax where) {
    // Allowed, this is the way to return in pascal.
  }

  @Override
  void checkWhetherFunction(PascalSyntax where) {}

  @Override
  void checkWhetherProcedure(PascalSyntax where) {
    where.error("func is not a procedure");
  }

  @Override
  void checkWhetherValue(PascalSyntax where) {
    // Skipping this since it depends on the context
    // A proper implementation would allow assignments in func
    // context but not elsewhere.
  }

  @Override
  void check(Block curScope, Library lib) {
    curScope.addDecl(name, this);
    block.outerScope = curScope;

    if(paramDeclList!=null) {
      paramDeclList.check(block, lib);
    }
    typeName.check(curScope, lib);
    type = typeName.type;
    block.check(curScope, lib);
  }

  @Override
  void genCode(CodeFile f) {
    declLevel = f.level+1;
    if(paramDeclList!=null) {
      paramDeclList.genCode(f);
    }
    block.genCode(f);
    f.genInstr("", "movl", "-32(%ebp),%eax", "Fetch return value");
    f.genInstr("", "leave", "", "End of " + name);
    f.genInstr("", "ret", "", "");
  }

}
