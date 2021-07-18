package parser;

import main.CodeFile;
import main.Main;

public class TypeDecl extends PascalDecl {

    TypeDecl(String id, int lNum) {
        super(id, lNum);
    }

    TypeDecl(String id, int lNum, types.Type type) {
        super(id, lNum);
        this.type = type;
    }

    @Override
    void check(Block curScope, Library lib) {

    }

    @Override
    public String identify() {
        return "<type decl> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyIndent();
        Main.log.prettyPrint(name + " :");
        //type.prettyPrint(); FIXME: ?
        Main.log.prettyPrintLn(";");
        Main.log.prettyOutdent();
    }

    @Override
    void checkWhetherAssignable(PascalSyntax where) {
        where.error("Cannot assign to type");
    }

    @Override
    void checkWhetherFunction(PascalSyntax where) {
        where.error("type is not a function");
    }

    @Override
    void checkWhetherProcedure(PascalSyntax where) {
        where.error("type is not a procedure");
    }

    @Override
    void checkWhetherValue(PascalSyntax where) {
        where.error("type is not a value");
    }

    @Override
    void genCode(CodeFile f) {
        
    }
}
