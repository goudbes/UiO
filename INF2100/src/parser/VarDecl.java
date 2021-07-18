package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class VarDecl extends PascalDecl {
    Type parserType;
    VarDeclPart context;

    public VarDecl(String id, int lNum) {
      super(id,lNum);
    }

    @Override public String identify() {
       return "<var decl> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyIndent();
        Main.log.prettyPrint(name + " :");
        parserType.prettyPrint();
        Main.log.prettyPrintLn(";");
        Main.log.prettyOutdent();
    }

    public static VarDecl parse(Scanner s) {
      enterParser("var decl");
      VarDecl varDecl = new VarDecl(s.curToken.id,s.curLineNum());
      varDecl.name = s.curToken.id;
      s.skip(nameToken);
      s.skip(colonToken);
      varDecl.parserType = Type.parse(s);
      varDecl.parserType.context = varDecl;
      s.skip(semicolonToken);
      leaveParser("var decl");
      return varDecl;
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {}

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("var is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("var is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {}

    @Override
    void check(Block curScope, Library lib) {
        parserType.check(curScope, lib);
        curScope.addDecl(name, this);
        type = parserType.type;
    }

    @Override
    void genCode(CodeFile f) {

    }
}
