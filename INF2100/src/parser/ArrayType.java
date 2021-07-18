package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ArrayType extends Type {
  Constant from;
  Constant to;

  public ArrayType(int lNum){
    super(lNum);
  }

  @Override
  public String identify() {
    return "<array-type> on line " + lineNum;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyPrint(type.identify());
  }

  public static ArrayType parse(Scanner s) {
    enterParser("array-type");

    s.skip(arrayToken);
    s.skip(leftBracketToken);
    ArrayType arrayType = new ArrayType(s.curLineNum());
    arrayType.from = Constant.parse(s);
    arrayType.from.context = arrayType;
    s.skip(rangeToken);
    arrayType.to = Constant.parse(s);
    arrayType.to.context = arrayType;
    s.skip(rightBracketToken);
    s.skip(ofToken);
    arrayType.type = new types.ArrayType(Type.parse(s).type, Library.integerType,
            arrayType.from.constVal, arrayType.to.constVal);
    // FIXME:
    // arrayType.type.context = arrayType;

    leaveParser("array-type");
    return arrayType;
  }

  @Override
  void check(Block curScope, Library lib) {
    from.check(curScope, lib);
    to.check(curScope, lib);
    from.type.checkType(Library.integerType, identify(), this, "Type mismatch");
    to.type.checkType(Library.integerType, identify(), this, "Type mismatch");
    ((types.ArrayType)type).loLim = from.constVal;
    ((types.ArrayType)type).hiLim = to.constVal;
    type.checkType(Library.arrayType, identify(), this, "Expected array type");
  }

  @Override
  void genCode(CodeFile f) {

  }
}
