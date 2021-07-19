public abstract class Expression {
    public abstract void interpret() throws UndeclaredVariableException;
    public abstract int getNumber() throws UndeclaredVariableException;
}

