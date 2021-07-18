package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
public abstract class Operator extends PascalSyntax {

  public Operator(int lNum) {
    super(lNum);
  }
}
