package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NumberedLiteral extends UnsignedConstant {

  public NumberedLiteral(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<number literal> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(Integer.toString(constVal));
  }

  public static NumberedLiteral parse(Scanner s) {
    enterParser("number literal");


    NumberedLiteral numberedLiteral = new NumberedLiteral(s.curLineNum());
    numberedLiteral.constVal = s.curToken.intVal;
    s.skip(intValToken);

    leaveParser("number literal");
    return numberedLiteral;
  }

  @Override
  void check(Block curScope, Library lib) {

  }

  @Override
  void genCode(CodeFile f) {
    f.genInstr("", "movl", String.format("$%d,%%eax", constVal), "  " + constVal);
  }
}
