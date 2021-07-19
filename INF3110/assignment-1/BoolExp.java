public class BoolExp extends Expression {
    private Expression first;
    private Expression second;
    private BoolOp operator;
    private int result;


     BoolExp(BoolOp operator, Expression first, Expression second) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    /**
     * Evaluates boolean expression
     * @throws UndeclaredVariableException
     */
    public void interpret() throws UndeclaredVariableException {
        first.interpret();
        second.interpret();
        switch (operator.getOp()) {
            case larger: result = first.getNumber() > second.getNumber() ? 1 : 0 ; break;
            case smaller: result = first.getNumber() < second.getNumber() ? 1 : 0; break;
            case equal: result = first.getNumber() == second.getNumber() ? 1 : 0; break;
        }
    }

    /**
     * Returns the result of the boolean expression, 1 if true, false otherwise
     * @return result
     */
    @Override
    public int getNumber() {
        return result;
    }
}
