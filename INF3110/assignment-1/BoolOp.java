public class BoolOp {

    public enum BooleanOp {
    smaller, larger, equal
    }

    /**
     * Returns boolean operator
     * @return operator
     */
     BooleanOp getOp() {
        return op;
    }

    private BooleanOp op;

    BoolOp(BooleanOp op) {
        this.op = op;
    }

}
