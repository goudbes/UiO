import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Monitor {

    private static int finalMax;
    //Final sorted and merged array to return
    public static int[] sorted;
    public static LinkedBlockingQueue<int[]> queue = new LinkedBlockingQueue<int[]>();

    /**
     * Gets the number of elements in the final sorted array
     *
     * @return number of elements
     */
    public static int getFinalMax() {
        return finalMax;
    }

    /**
     * Sets the number of elements in the final sorted array
     * @param f the number of elements
     */
    public static void setFinalMax(int f) {
        finalMax = f;
    }

    /**
     * Returns a pair of arrays to be further merged
     * Method take() Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     */
    public static synchronized ArrayList<int[]> getPair() throws InterruptedException {
        //Those arrays are going to be returned in arrayList
        ArrayList<int[]> pair = new ArrayList<int[]>();
        //Taking first array
        pair.add(queue.take());

        //Making sure the first array isn't the final array, or it is :)
        if (pair.get(0).length == getFinalMax()) {
            sorted = pair.get(0);
            return null;
        }
        //Taking the second array
        pair.add(queue.take());
        return pair;
    }

}
