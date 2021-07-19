public class VarDecl {
    private Identifier identifier;
    private Expression expression;
    private int value;

    public VarDecl(Identifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    /**
     * Returns the value of the declaration
     * @return value
     */
    public int getValue() {
       return value;
    }

    /**
     * Returns identifier
     * @return identifier
     */
    public String getIdentifier() {
        return identifier.getIdentifier();
    }

    /**
     * Interprets the expression and saves its value
     * @throws UndeclaredVariableException
     */
    public void interpret() throws UndeclaredVariableException {
        expression.interpret();
        value = expression.getNumber();
    }

    /**
     * Sets the value for the identifier
     * @param value value
     */
    public void setValue(int value) {
        this.value = value;
    }
}
