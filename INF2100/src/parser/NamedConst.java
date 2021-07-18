package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NamedConst extends UnsignedConstant {

  String name;

  public NamedConst(int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<named constant> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(name);
  }

  public static NamedConst parse(Scanner s) {
    enterParser("named constant");

    Pattern p = Pattern.compile("[a-z][a-z0-9]*");
    Matcher m = p.matcher(s.curToken.id);
    if(!m.matches()){
      s.testError(s.curToken.kind.toString());
    }

    NamedConst namedConst = new NamedConst(s.curLineNum());
    namedConst.name = s.curToken.id;
    s.skip(nameToken);

    leaveParser("named constant");
    return namedConst;
  }

  @Override
  void check(Block curScope, Library lib) {
    PascalDecl d = curScope.findDecl(name, this);
    d.checkWhetherValue(this);
    if(d instanceof ConstDecl) {
      constVal = ((ConstDecl) d).constant.constVal;
    }
    type = d.type;
  }

  @Override
  void genCode(CodeFile f) {
    f.genInstr("", "movl", String.format("$%d,%%eax", constVal), Integer.toString(constVal));
    f.genInstr("", "pushl", "%eax", "push param");
  }
}
