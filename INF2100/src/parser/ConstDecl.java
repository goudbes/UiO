package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ConstDecl extends PascalDecl {
    Constant constant;
    ConstDeclPart context = null;

    public ConstDecl(String id, int lNum) {
      super(id,lNum);
    }

    public ConstDecl(String id, int lNum, types.Type type) {
        super(id,lNum);
        this.type = type;
    }

    @Override public String identify() {
       return "<const decl> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyIndent();
        Main.log.prettyPrint(name + " = ");
        constant.prettyPrint();
        Main.log.prettyPrintLn(";");
        Main.log.prettyOutdent();
    }

    public static ConstDecl parse(Scanner s) {
      enterParser("const decl");
      s.test(nameToken);
      ConstDecl constDecl = new ConstDecl(s.curToken.id,s.curLineNum());
      constDecl.name = s.curToken.id;
      s.readNextToken();
      s.skip(equalToken);
      constDecl.constant = Constant.parse(s);
      constDecl.constant.context = constDecl;
      s.skip(semicolonToken);
      leaveParser("const decl");
      return constDecl;
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Cannot assign to const");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("const is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("var is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {}

    @Override
    void check(Block curScope, Library lib) {
        constant.check(curScope, lib);
        curScope.addDecl(name, this);
        type = constant.type;
    }

    @Override
    void genCode(CodeFile f) {
        constant.genCode(f);
    }
}
