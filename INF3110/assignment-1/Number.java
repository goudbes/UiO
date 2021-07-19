public class Number extends Expression {

    private int number;

     Number(int n) {
        number = n;
    }

    /**
     * Gets the number
     * @return number
     */
    public int getNumber() {
        return number;
    }

    public void interpret() {
    }
}
