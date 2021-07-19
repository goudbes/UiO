import java.util.ArrayList;
import java.util.List;

public class While extends Statement {

    private BoolExp condition;
    private List<Statement> statements;

    public While(BoolExp condition) {
        this.condition = condition;
        statements = new ArrayList<>();
    }

    /**
     * Adding statements inside the while expression
     * @param statement statements
     */
    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    /**
     * Interprets the boolean condition and all the statements
     * @throws UndeclaredVariableException
     * @throws OutOfGridException
     */
    public void interpret() throws UndeclaredVariableException, OutOfGridException {
        condition.interpret();
        while (condition.getNumber() == 1) {
            for (Statement s: statements) {
                s.interpret();
            }
            condition.interpret();
        }
    }
}
