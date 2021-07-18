package types;

import parser.PascalSyntax;

public class ArrayType extends Type {
    public Type elemType, indexType;
    public int loLim, hiLim;

    public ArrayType(Type e, Type i, int lo, int hi) {
	    elemType = e;
	    indexType = i;
		loLim = lo;
		hiLim = hi;
    }

	/**
	 * Constructor without arguments for type checking use in the Library
	 */
	public ArrayType() {}

    @Override public String identify() {
		if(elemType == null || indexType == null) {
			return "type array []";
		}
		return "type array [" + loLim + ".." + hiLim + ": " +
				indexType.identify() + "] of " + elemType.identify();
    }

    @Override public void checkType(Type tx, String op, 
				    PascalSyntax where, String message) {
		if (tx instanceof ArrayType) {
			ArrayType txa = (ArrayType)tx;
			if(txa.elemType != null) {
				indexType.checkType(txa.indexType, "array index", where, message);
				elemType.checkType(txa.elemType, op, where, message);
			}
		} else {
			where.error(message);
		}
    }


    @Override public int size() {
	return (hiLim-loLim+1)*elemType.size();
    }
}
