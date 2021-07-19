public class ArithmeticExp extends Expression {

    private ArithmeticOp operator;
    private Arguments arguments;
    private int result;

    ArithmeticExp(ArithmeticOp operator, Arguments args) {
        this.operator = operator;
        this.arguments = args;
    }

    /**
     * Calculates arithmetic expression
     * @throws UndeclaredVariableException
     */
    public void interpret() throws UndeclaredVariableException {
        arguments.interpret();
        result = arguments.getExpressions().get(0).getNumber();
        for (int i = 1; i < arguments.getExpressions().size(); i++) {
            switch (operator.getOp()) {
                case plus:  result += arguments.getExpressions().get(i).getNumber(); break;
                case minus: result -= arguments.getExpressions().get(i).getNumber(); break;
                case product: result *= arguments.getExpressions().get(i).getNumber(); break;
            }
        }
    }

    /**
     * Returns the result of arithmetic expression
     * @return
     */
    @Override
    public int getNumber() {
        return result;
    }
}
