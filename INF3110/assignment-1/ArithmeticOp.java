public class ArithmeticOp {

    public enum ArOp {
        plus, minus, product
    }

    private ArOp op;

    ArithmeticOp(ArOp op) {
        this.op = op;
    }

    /**
     * Returns arithmetic operator
     * @return operator
     */
    ArOp getOp() {
        return op;
    }
}
