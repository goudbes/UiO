package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ParamDecl extends PascalDecl {
  String name;
  TypeName typeName;

  public ParamDecl(String name, int lNum) {
    super(name, lNum);
  }

  @Override
  public String identify() {
    return "<param decl> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" " + name + ":");
    typeName.prettyPrint();
  }

  public static ParamDecl parse(Scanner s) {
    enterParser("param decl");
    ParamDecl paramDecl = new ParamDecl(s.curToken.id, s.curLineNum());
    paramDecl.name = s.curToken.id;
    s.skip(nameToken);

    s.skip(colonToken);

    paramDecl.typeName = TypeName.parse(s);
    paramDecl.typeName.context = paramDecl;

    leaveParser("param decl");
    return paramDecl;
  }

  @Override
  void checkWhetherAssignable(PascalSyntax where) {
    where.error("Cannot assign to param");
  }

  @Override
  void checkWhetherFunction(PascalSyntax where) {
    where.error("param is not a function");
  }

  @Override
  void checkWhetherProcedure(PascalSyntax where) {
    where.error("param is not a procedure");
  }

  @Override
  void checkWhetherValue(PascalSyntax where) {}

  @Override
  void check(Block curScope, Library lib) {
    typeName.check(curScope, lib);
    type = typeName.type;
    curScope.addDecl(name, this);
  }

  @Override
  void genCode(CodeFile f) {

  }
}
