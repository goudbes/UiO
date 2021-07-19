public class Start {
    private Expression first;
    private Expression second;

    public Start(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the value of the first expression
     * @return value
     * @throws UndeclaredVariableException
     */
    public int getX() throws UndeclaredVariableException {
        return first.getNumber();
    }

    /**
     * Gets the value of the second expression
     * @return value
     * @throws UndeclaredVariableException
     */
    public int getY() throws UndeclaredVariableException {
        return second.getNumber();
    }

    /**
     * Interprets two expressions
     * @throws UndeclaredVariableException
     */
    public void interpret() throws UndeclaredVariableException {
        first.interpret();
        second.interpret();
    }
}
