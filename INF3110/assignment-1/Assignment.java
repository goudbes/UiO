public class Assignment extends Statement {
    private Identifier identifier;
    private Expression expression;

    public Assignment(Identifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    /**
     * Performs the assignment on identifier
     * @throws UndeclaredVariableException if identifier wasn't found
     */
    public void interpret() throws UndeclaredVariableException {
        expression.interpret();
        Robot robot = Program.program.getRobot();
        String ident = identifier.getIdentifier();
        for (VarDecl v: robot.getDeclarations()) {
            if (ident.equalsIgnoreCase(v.getIdentifier())) {
                v.setValue(expression.getNumber());
                return;
            }
        }
        throw new UndeclaredVariableException("Identifier " + ident + " wasn't found");
    }
}
