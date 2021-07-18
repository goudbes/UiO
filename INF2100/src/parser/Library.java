package parser;

import main.CodeFile;
import types.*;
import types.ArrayType;

public class Library extends Block {

    static IntType integerType = new IntType();
    static CharType charType = new CharType();
    static types.ArrayType arrayType = new ArrayType();
    static BoolType boolType = new BoolType();

    public Library(int lNum) {
        super(lNum);

        // Predefined names
        decls.put("boolean", new TypeDecl("boolean", lNum, boolType));
        decls.put("char", new TypeDecl("char", lNum, charType));
        decls.put("integer", new TypeDecl("integer", lNum, integerType));
        decls.put("false", new ConstDecl("false", lNum, boolType));
        decls.put("true", new ConstDecl("true", lNum, boolType));
        decls.put("write", new ProcDecl("write", lNum));

        ConstDecl c_true = new ConstDecl("true", lNum, boolType);
        Constant true_constant = new Constant(lNum);
        true_constant.constVal = 1;
        c_true.constant = true_constant;
        decls.put("true", c_true);

        ConstDecl c_false = new ConstDecl("false", lNum, boolType);
        Constant false_constant = new Constant(lNum);
        false_constant.constVal = 0;
        c_false.constant = false_constant;
        decls.put("false", c_false);

        ConstDecl eol = new ConstDecl("eol", lNum, charType);
        Constant constant = new Constant(lNum);
        constant.constVal = 10;
        eol.constant = constant;
        decls.put("eol", eol);
    }

    @Override
    public void genCode(CodeFile f) {

    }
}
