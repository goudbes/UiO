import java.util.List;

public class Arguments {

    //List with expressions
    private List<Expression> expressions;

     Arguments(List<Expression> list) {
        expressions = list;
    }

    /**
     * Interprets expressions inside args
     * @throws UndeclaredVariableException
     */
    public void interpret() throws UndeclaredVariableException {
        for (Expression e: expressions) {
            e.interpret();
        }
    }

    /**
     * Returns the list with expressions
     * @return list with expressions
     */
    public List<Expression> getExpressions() {
        return expressions;
    }
}
