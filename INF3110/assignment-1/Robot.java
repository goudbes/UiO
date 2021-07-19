import java.util.ArrayList;

public class Robot implements Robol {
    //List with all the declarations
    private ArrayList<VarDecl> varDecls;
    //List with statements
    private ArrayList<Statement> statementList;
    //Start position
    private Start start;
    private int xPos, yPos;

    public Robot() {
        varDecls = new ArrayList<>();
        statementList = new ArrayList<>();
    }

    /**
     * Gets the position on x axis
     * @return position
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Gets the position on y axis
     * @return position
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * Moving the robot on the grid
     * @param x how far to move on x axis
     * @param y how far to move on y axis
     * @throws OutOfGridException
     */
    public void move(int x, int y) throws OutOfGridException {
        setPos(getxPos() +x, getyPos() + y);
    }

    /**
     * Sets the position for the robot
     * @param xPos x axis position
     * @param yPos y axis position
     * @throws OutOfGridException
     */
    public void setPos(int xPos, int yPos) throws OutOfGridException {
        if (Program.program.getGrid().isInGrid(xPos,yPos)) {
            this.xPos = xPos;
            this.yPos = yPos;
            System.out.println("Robot is now at position ("+ xPos + "," + yPos+")");
        } else {
            throw new OutOfGridException("Position is outside of the grid");
        }
    }

    /**
     * Adding start position
     * @param start starting point
     */
    public void addStart(Start start) {
        this.start = start;
    }

    /**
     * Adding declarations for the robot
     * @param varDecl declaration
     */
    public void addDeclaration(VarDecl varDecl) {
        varDecls.add(varDecl);
    }

    /**
     * Returns list of declarations
     * @return list of declarations
     */
    public ArrayList<VarDecl> getDeclarations() {
        return varDecls;
    }

    /**
     * Adding statements to the robot
     * @param statement statement
     */
    public void addStatement(Statement statement) {
        statementList.add(statement);
    }

    /**
     * Interpreting the declarations, statements, and start
     * @throws UndeclaredVariableException
     * @throws OutOfGridException
     */
    public	void interpret() throws UndeclaredVariableException, OutOfGridException {
        for (VarDecl v: varDecls) {
            v.interpret();
        }
        start.interpret();
        setPos(start.getX(),start.getY());
        for (Statement s: statementList) {
            s.interpret();
        }
        System.out.println("Robot is at position(" + getxPos() +"," + getyPos() +")");
        System.out.println("********************************");
    }
}
