public class Move extends Statement {

    private Expression expression;

    public enum Direction {
        north, south, east, west
    }

    private Direction direction;

     Move(Direction direction, Expression expression) {
        this.expression = expression;
        this.direction = direction;
    }

    /**
     * Gets the direction
     * @return direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Interprets the expression and moves the robot according to the direction and value
     * @throws UndeclaredVariableException
     * @throws OutOfGridException
     */
    public void interpret() throws UndeclaredVariableException, OutOfGridException {
        expression.interpret();
        Robot robot = Program.program.getRobot();
        switch (direction) {
            case east: robot.move(expression.getNumber(), 0); break;
            case west: robot.move(-1 * expression.getNumber(), 0); break;
            case north: robot.move(0, expression.getNumber()); break;
            case south: robot.move(0, -1 * expression.getNumber()); break;
        }
    }
}
