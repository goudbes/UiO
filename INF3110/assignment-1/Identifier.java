public class Identifier extends Expression {

    private String identifier;

    Identifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns identifier
     * @return identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    public void interpret() {}

    /**
     * Returns the value of identifier
     * @return value
     * @throws UndeclaredVariableException
     */
    @Override
    public int getNumber() throws UndeclaredVariableException {
        Robot robot = Program.program.getRobot();
        for (VarDecl v : robot.getDeclarations()) {
            if (v.getIdentifier().equalsIgnoreCase(identifier)) {
                return v.getValue();
            }
        }
        throw new UndeclaredVariableException("Identifier " + identifier + " wasn't found");
    }
}
