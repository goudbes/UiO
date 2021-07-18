package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TypeName extends Type {
  String name;

  public TypeName(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<type name> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(" " + name);
  }

  public static TypeName parse(Scanner s) {
    enterParser("type name");
    TypeName typeName = null;
    if(s.curToken.kind == nameToken) {
      if(s.curToken.id.equals("boolean") || s.curToken.id.equals("integer")
      || s.curToken.id.equals("char")){
        typeName = new TypeName(s.curLineNum());
        typeName.name = s.curToken.id;
        switch(typeName.name){
          case "boolean":
            typeName.type = Library.boolType;
            break;
          case "integer":
            typeName.type = Library.integerType;
            break;
          case "char":
            typeName.type = Library.charType;
            break;
        }
        s.skip(nameToken);
      } else {
        s.testError("boolean, integer or char");
      }
    } else {
      s.testError(s.curToken.kind.toString());
    }
    leaveParser("type name");
    return typeName;
  }

  @Override
  void check(Block curScope, Library lib) {
    PascalDecl d = curScope.findDecl(name, this);
    type = d.type;
  }

  @Override
  void genCode(CodeFile f) {

  }
}
