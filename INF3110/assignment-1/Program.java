import java.util.Arrays;

public class Program implements Robol {

    static Program program;
    private Grid grid;
    private Robot robot;

     Program(Grid grid, Robot robot) {
        this.grid	=	grid;
        this.robot	=	robot;
        program = this;
    }

    /**
     * Returns grid
     * @return grid
     */
    Grid getGrid() {
        return grid;
    }

    /**
     * Returns robot
     * @return robot
     */
    Robot getRobot() {
        return robot;
    }

    /**
     * Interprets robot
     * @throws UndeclaredVariableException
     * @throws OutOfGridException
     */
    public void interpret() throws UndeclaredVariableException, OutOfGridException {
        robot.interpret();
    }

    public static void main(String[] args) throws UndeclaredVariableException, OutOfGridException {
        testOne();
        testTwo();
        testThree();
        //testFour();
    }

    public static void testOne() throws UndeclaredVariableException, OutOfGridException {
        System.out.println("\n Testing code #1");
        Program p = new Program(new Grid(64,64),new Robot());
        Start start = new Start(new Number(23), new Number(30));
        p.getRobot().addStart(start);
        p.getRobot().addStatement(new Move(Move.Direction.west, new Number(15)));
        p.getRobot().addStatement(new Move(Move.Direction.south, new Number(15)));
        p.getRobot().addStatement(
                new Move(Move.Direction.east,
                new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.plus),
                        new Arguments(Arrays.asList(new Number(2), new Number(3))))));
        p.getRobot().addStatement(
                new Move(Move.Direction.north,
                        new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.plus),
                                new Arguments(Arrays.asList(new Number(10), new Number(27))))));
        p.getRobot().addStatement(new Stop());
        p.interpret();
    }

    public static void testTwo() throws UndeclaredVariableException, OutOfGridException {
        System.out.println("\n Testing code #2");
        Program p = new Program(new Grid(64,64),new Robot());
        p.getRobot().addDeclaration(new VarDecl(new Identifier("i"),new Number(5)));
        p.getRobot().addDeclaration(new VarDecl(new Identifier("j"),new Number(3)));
        Start start = new Start(new Number(23), new Number(6));
        p.getRobot().addStart(start);
        p.getRobot().addStatement(
                new Move(Move.Direction.north,
                        new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.product),
                                new Arguments(Arrays.asList(new Number(3), new Identifier("i"))))));
        p.getRobot().addStatement(new Move(Move.Direction.east, new Number(15)));
        p.getRobot().addStatement(new Move(Move.Direction.south, new Number(4)));
        ArithmeticExp first = new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.product),
                new Arguments(Arrays.asList(new Number(2), new Identifier("i"))));
        ArithmeticExp second = new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.product),
                new Arguments(Arrays.asList(new Number(3), new Identifier("j"))));
        p.getRobot().addStatement(new Move(Move.Direction.west, new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.plus),
                new Arguments(Arrays.asList(first,second,new Number(5))))));
        p.getRobot().addStatement(new Stop());
        p.interpret();
    }

    public static void testThree() throws UndeclaredVariableException, OutOfGridException {
        System.out.println("\n Testing code #3");
        Program p = new Program(new Grid(64,64),new Robot());
        p.getRobot().addDeclaration(new VarDecl(new Identifier("i"),new Number(5)));
        p.getRobot().addDeclaration(new VarDecl(new Identifier("j"),new Number(3)));
        Start start = new Start(new Number(23), new Number(6));
        p.getRobot().addStart(start);
        p.getRobot().addStatement(
                new Move(Move.Direction.north,
                        new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.product),
                                new Arguments(Arrays.asList(new Number(3), new Identifier("i"))))));
        p.getRobot().addStatement(new Move(Move.Direction.west, new Number(15)));
        p.getRobot().addStatement(new Move(Move.Direction.east, new Number(4)));
        While cond = new While(new BoolExp(new BoolOp(BoolOp.BooleanOp.larger),
                new Identifier("j"), new Number(0)));
        cond.addStatement(new Move(Move.Direction.south,new Identifier("j")));
        cond.addStatement(new Assignment(new Identifier("j"),new ArithmeticExp(new ArithmeticOp(ArithmeticOp.ArOp.minus),
                new Arguments(Arrays.asList(new Identifier("j"),new Number(1))))));
        p.getRobot().addStatement(cond);
        p.getRobot().addStatement(new Stop());
        p.interpret();

    }

    public static void testFour() throws UndeclaredVariableException, OutOfGridException {
        System.out.println("\n Testing code #4");
        Program p = new Program(new Grid(64,64),new Robot());
        p.getRobot().addDeclaration(new VarDecl(new Identifier("j"),new Number(3)));
        Start start = new Start(new Number(1), new Number(1));
        p.getRobot().addStart(start);
        While cond = new While(new BoolExp(new BoolOp(BoolOp.BooleanOp.larger),
                new Identifier("j"), new Number(0)));
        cond.addStatement(new Move(Move.Direction.north,new Identifier("j")));
        p.getRobot().addStatement(cond);
        p.getRobot().addStatement(new Stop());
        p.interpret();
    }

}
