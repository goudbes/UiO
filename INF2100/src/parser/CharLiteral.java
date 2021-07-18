package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CharLiteral extends UnsignedConstant {
  char value;

  public CharLiteral(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<char literal> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint("'");
    if(value == '\''){
      Main.log.prettyPrint("''");
    } else {
      Main.log.prettyPrint(Character.toString(value));
    }
    Main.log.prettyPrint("'");
  }

  public static CharLiteral parse(Scanner s) {
    enterParser("char literal");

    CharLiteral charLiteral = new CharLiteral(s.curLineNum());
    charLiteral.value = s.curToken.charVal;
    s.skip(charValToken);
    // Special case, should only store one ' for ''
    if(s.curToken.charVal == '\''){
      s.skip(charValToken);
    }

    leaveParser("char literal");
    return charLiteral;
  }

  @Override
  void check(Block curScope, Library lib) {

  }

  @Override
  void genCode(CodeFile f) {
    f.genInstr("", "movl", String.format("$%d,%%eax", (int) value), "  '" + Character.toString(value) + "'");
  }
}

