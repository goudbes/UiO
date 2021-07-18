package parser;
import main.*;
import scanner.*;
import types.IntType;

import static scanner.TokenKind.*;
public class Constant extends PascalSyntax {
  PrefixOperator prefixOperator;
  UnsignedConstant unsignedConstant;
  PascalSyntax context;
  types.Type type;
  int constVal;

  public Constant (int lNum) {
    super(lNum);
  }

  @Override
  public String identify() {
    return "<constant> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    if(prefixOperator!=null) {
      prefixOperator.prettyPrint();
    }
    unsignedConstant.prettyPrint();
  }

  public static Constant parse(Scanner s) {
    enterParser("constant");
    Constant constant = new Constant(s.curLineNum());

    if(s.curToken.kind == addToken || s.curToken.kind == subtractToken) {
      constant.prefixOperator = PrefixOperator.parse(s);
      constant.prefixOperator.context = constant;
    }

    constant.unsignedConstant = UnsignedConstant.parse(s);
    constant.unsignedConstant.context = constant;

    leaveParser("constant");
    return constant;
  }

  @Override
  void check(Block curScope, Library lib) {
    unsignedConstant.check(curScope, lib);
    type = unsignedConstant.type;
    constVal = unsignedConstant.constVal;
    if(prefixOperator!=null){
      String oprName = prefixOperator.opr.toString();
      unsignedConstant.type.checkType(Library.integerType, "Prefix " + oprName, this,
              "Prefix + or - may only be applied to integers.");
      if(prefixOperator.opr == subtractToken) {
        constVal = -constVal;
      }
    }
  }

  @Override
  void genCode(CodeFile f) {
    //if(prefixOperator.opr == subtractToken) {
    //  f.genInstr("", "negl", "%eax", "  - (prefix)");
    //}
  }
}
