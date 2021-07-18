import java.util.ArrayList;

public class SortingThread implements Runnable {

    private int id;
    private int v;
    private int h;
    private int[] a;
    private int number;

    public SortingThread(int id, int v, int h, int[] a, int number) {
        this.id = id;
        this.v = v;
        this.h = h;
        this.a = a;
        this.number = number;
    }

    public int getID() {
        return this.id;
    }

    public void run() {
        try {
            //Creating a new array to store the sorted part
            int[] sorted = new int[number];
            //Finding max elements
            a = finnMax(a, number, v, h);
            //Copying them from the original array
            for (int i = v; i <= v + number - 1; i++) {
                sorted[i - v] = a[i];
            }
            //Adding the results to the monitor queue for further merging
            Monitor.queue.add(sorted);
            ArrayList<int[]> pair = Monitor.getPair();
            //Merging it if it's not null
            if (pair != null) {
                Monitor.queue.add(merge(pair.get(0), pair.get(1)));
            }
        } catch (Exception e) {
            System.out.println(e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Finding the largest k elements in the array
     *
     * @param a                        input array
     * @param numberOfRelevanceResults Number of relevant results
     * @param v                        lower bound of the array
     * @param h                        upper bound of the array
     * @return sorted array
     */
    public int[] finnMax(int[] a, int numberOfRelevanceResults, int v, int h) {
        //Sorting a[0..k-1]
        int tmp;
        insertSort(a, v, v + numberOfRelevanceResults - 1);
        for (int j = v + numberOfRelevanceResults; j <= h; j++) {
            if (a[v + numberOfRelevanceResults - 1] < a[j]) {
                tmp = a[j];
                a[j] = a[v + numberOfRelevanceResults - 1];
                a[v + numberOfRelevanceResults - 1] = tmp;
                insertSortOneElement(a, v, v + numberOfRelevanceResults - 1);
            }
        }
        return a;
    }

    /**
     * Insertion sort
     *
     * @param a input array
     * @param v lower bound of the array
     * @param h upper bound of the array
     */
    public void insertSort(int[] a, int v, int h) {
        int i, t;
        for (int k = v; k < h; k++) {
            t = a[k + 1];
            i = k;
            while (i >= v && a[i] < t) {
                a[i + 1] = a[i];
                i--;
            }
            a[i + 1] = t;
        }
    }

    /**
     * Sorts the rightmost element into previously sorted part
     *
     * @param a array
     * @param v lower bound of the array
     * @param h upper bound (the rightmost element)
     */
    public void insertSortOneElement(int[] a, int v, int h) {
        int tmp;
        for (int i = h - 1; i >= v; i--) {
            if (a[h] >= a[i]) {
                tmp = a[h];
                for (int j = h; j > i; j--) {
                    a[j] = a[j - 1];
                }
                a[i] = tmp;
                h--;
            }
        }
    }

    /**
     * Method that merges two sorted arrays into one.
     * Taken from: http://stackoverflow.com/a/8949433
     *
     * @param a first array
     * @param b second array
     * @return merged array
     */
    public static int[] merge(int[] a, int[] b) {
        int[] answer = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length)
            answer[k++] = a[i] > b[j] ? a[i++] : b[j++];

        while (i < a.length)
            answer[k++] = a[i++];

        while (j < b.length)
            answer[k++] = b[j++];
        return answer;
    }
}
