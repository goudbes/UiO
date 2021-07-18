package parser;
import main.*;
import scanner.*;
import types.*;
import types.ArrayType;

import static scanner.TokenKind.*;

public abstract class UnsignedConstant extends Factor {

  int constVal;

  public UnsignedConstant(int lNum) {
    super(lNum);
  }

  public static UnsignedConstant parse(Scanner s) {
    enterParser("unsigned constant");
    UnsignedConstant unsignedConstant = null;
    switch(s.curToken.kind) {
      case nameToken:
      unsignedConstant = NamedConst.parse(s);
      // Unknown type - declared elsewhere
      break;
      case intValToken:
      unsignedConstant = NumberedLiteral.parse(s);
      unsignedConstant.type = Library.integerType;
      break;
      case charValToken:
      unsignedConstant = CharLiteral.parse(s);
      unsignedConstant.type = Library.charType;
      break;
      default:
      s.testError(s.curToken.kind.toString());
      break;
    }
    leaveParser("unsigned constant");
    return unsignedConstant;
  }

}
