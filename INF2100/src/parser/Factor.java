package parser;
import main.*;
import scanner.*;
import types.*;

import static scanner.TokenKind.*;

public abstract class Factor extends PascalSyntax  {
  types.Type type;

  public Factor(int lNum) {
    super(lNum);
  }

  public static Factor parse(Scanner s){
    enterParser("factor");
    Factor factor = null;
    switch(s.curToken.kind){
      case intValToken:
      case charValToken:
        factor = UnsignedConstant.parse(s);
        break;
      case nameToken:
      if(s.nextToken.kind == leftParToken){
        factor = FuncCall.parse(s);
      } else {
        // If it's not a [ we can not be sure
        // but assuming for now that it's a variable.
        // Could be either Variable, UnsignedConstant or FuncCall
        factor = Variable.parse(s);
      }
      break;
      case leftParToken:
      factor = InnerExpr.parse(s);
      break;
      case notToken:
      factor = Negation.parse(s);
      break;
      default:
      s.testError("value");
    }
    leaveParser("factor");
    return factor;
  }

}
