public class Grid {

    private int width, height;

    Grid( int width, int height) throws OutOfGridException {
        setHeight(height);
        setWidth(width);
    }

    /**
     * Returns the width of the grid
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the grid
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the width of the grid
     * @param width width
     */
    public void setWidth(int width) throws OutOfGridException {
        if (width >= 0) {
            this.width = width;
        } else {
            System.out.println("Grid width should be > = 0");
            throw new OutOfGridException("Invalid value");
        }
    }

    /**
     * Setting the height of the grid
     * @param height height
     */
    public void setHeight(int height) throws OutOfGridException {
        if (width >= 0) {
            this.height = height;
        } else {
            System.out.println("Grid height should be > = 0");
            throw new OutOfGridException("Invalid value");
        }
    }

    /**
     * Checks if the robot is inside the grid
     * @param x position x
     * @param y position y
     * @return true if is in the grid, false otherwise
     */
    public boolean isInGrid(int x, int y) {
        return ((x >= 0 && x <= width) && (y >= 0 && y <= height));
    }

    public void interpret() {

    }
}
