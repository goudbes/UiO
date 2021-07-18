package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public abstract class Type extends PascalSyntax {

  types.Type type;

  public Type(int lNum) {
    super(lNum);
  }

  public static Type parse(Scanner s) {
    enterParser("type");
    Type type;
    if(s.curToken.kind == arrayToken){
      type = ArrayType.parse(s);
    }
    else{
      type = TypeName.parse(s);
    }
    leaveParser("type");
    return type;
  }

}
