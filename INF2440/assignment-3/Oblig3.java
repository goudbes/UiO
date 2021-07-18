
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class Oblig3 {

    public static ArrayList<Double> seqTime = new ArrayList<>();
    public static ArrayList<Double> parTime = new ArrayList<>();


    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Invalid number of arguments: >java -Xmx16000m Oblig3 <n>");
            System.exit(1);
        } else {
            int n = Integer.parseInt(args[0]);
            if (n <= 0 || n < 2) {
                System.out.println("Invalid n. Exiting...");
                System.exit(1);
            }


            int run = 3;

            for (int i = 0; i < run; i++) {
                System.out.println("\nKj\u00F8ring #" + (i + 1));
                System.out.println("\n--- Radix sort, sequential implementation ---");
                MultiRadix multiRadix = new MultiRadix();
                multiRadix.doIt(n);

                if (n > 10) {
                    System.out.println("\n--- Radix sort, parallel implementation ---");
                    MultiRadixP multiRadixP = new MultiRadixP();
                    multiRadixP.solve(n);
                }
            }

            System.out.println("\nRadix sort seq. median " + median(seqTime));
            System.out.println("Radix sort par. median " + median(parTime));
            System.out.println("Radix sort speed up " + median(seqTime) / median(parTime));
        }
    }

    /**
     * Returens the median of an arraylist
     *
     * @param list input arraylist
     * @return median number
     */
    public static double median(ArrayList<Double> list) {
        Collections.sort(list);
        int mid = list.size() / 2;
        double median = list.get(mid);
        if (list.size() % 2 == 0) {
            median = (median + list.get(mid - 1)) / 2;
        }
        return median;
    }

    /**
     *
     */
    public static class MultiRadixP {
        int n;
        int[] a;
        final static int NUM_BIT = 7;

        public void solve(int len) {
            a = new int[len];
            Random r = new Random(123);
            for (int i = 0; i < len; i++) {
                a[i] = r.nextInt(len);
            }
            try {
                a = radixMultiP(a);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        /**
         * Precalculations for the radix sort in parallell
         *
         * @param a array to sort
         * @return sorted array
         * @throws InterruptedException
         */
        int[] radixMultiP(int[] a) throws InterruptedException {
            long startTime = System.nanoTime();
            int max = a[0], numBit = 2, numDigits, n = a.length;
            int[] bit;

            //long startTime = System.nanoTime();
            max = findMaxPar(a);

            //System.out.println("Find max parallel took (ms) : " + (System.nanoTime() - startTime) / 1000000.0);
            //System.out.println("Find max parallel found: " + max);
            while (max >= (1L << numBit)) numBit++;

            numDigits = Math.max(1, numBit / NUM_BIT);
            bit = new int[numDigits];
            int rest = (numBit % numDigits), sum = 0;

            for (int i = 0; i < bit.length; i++) {
                bit[i] = numBit / numDigits;
                if (rest-- > 0) bit[i]++;
            }

            int[] t = a, b = new int[n];

            for (int i = 0; i < bit.length; i++) {
                radixSort(a, b, bit[i], sum, bit[i]);
                sum += bit[i];
                t = a;
                a = b;
                b = t;
            }
            if (bit.length % 2 != 0) {
                System.arraycopy(a, 0, b, 0, a.length);
            }

            double tid = (System.nanoTime() - startTime) / 1000000.0;
            parTime.add(tid);
            System.out.println("Sorted  " + n + " numbers in parallel:" + tid + "ms.");
            testSort(a);
            return a;
        }

        /**
         * Testing if the array is sorted
         *
         * @param a array
         */
        void testSort(int[] a) {
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] > a[i + 1]) {
                    System.out.println("Sorting error on place: " + i + " a[" + i + "]:" + a[i] + " > a[" + (i + 1) + "]:" + a[i + 1]);
                    return;
                }
            }
        }

        /**
         * Sorting the array in parallel
         *
         * @param a       array to sort
         * @param b       array to copy the results to
         * @param maskLen mask Length
         * @param shift   shift
         * @param numBit  number of digits in binary max
         */
        void radixSort(int[] a, int[] b, int maskLen, int shift, int numBit) {
            System.out.println("  \u2022 radixSort maskLen: " + maskLen + ", shift: " + shift);
            int mask = (1 << maskLen) - 1;

            // b) count=the frequency of each radix value in a
            ArrayList<Thread> threads = new ArrayList<>();
            int numberOfThreads = Runtime.getRuntime().availableProcessors();
            CyclicBarrier barry = new CyclicBarrier(numberOfThreads);
            int[][] allCount = new int[numberOfThreads][];
            int numSif = (int) Math.pow(2, numBit);
            int[] sumCount = new int[numSif];
            int elementsToSort = (int) Math.ceil((double) a.length / numberOfThreads);
            int threadId = 0;
            for (int i = 0; i < a.length; i = i + elementsToSort) {
                if (a.length < i + elementsToSort) {
                    elementsToSort = a.length - i;
                }
                Thread t = new Thread(new RadixThread(threadId++, numSif, i, i + elementsToSort - 1,
                        a, b, shift, mask, allCount, barry, sumCount));
                threads.add(t);
                t.start();
            }

            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }// end radixSort

        public static class RadixThread implements Runnable {
            private int id;
            private int[] count;
            private int lo;
            private int hi;
            private int[] a;
            private int[] b;
            private int mask;
            private int shift;
            private int[][] allCount;
            private CyclicBarrier barry;
            private int[] sumCount;

            public RadixThread(int id, int numSif, int lo, int hi, int[] a, int[] b,
                               int shift, int mask, int[][] allCount,
                               CyclicBarrier barry, int[] sumCount) {
                this.id = id;
                this.count = new int[numSif];
                this.lo = lo;
                this.hi = hi;
                this.a = a;
                this.b = b;
                this.mask = mask;
                this.shift = shift;
                this.allCount = allCount;
                this.barry = barry;
                this.sumCount = sumCount;
            }

            public void run() {
                int numberOfThreads = Runtime.getRuntime().availableProcessors();
                try {
                    for (int i = lo; i <= hi; i++) {
                        count[(a[i] >>> shift) & mask]++;
                    }
                    allCount[id] = count;
                    barry.await();

                    //DEL C
                    int[] localCount = new int[count.length];
                    int acumval = 0;
                    for (int t = 0; t < count.length; t++) {
                        for (int r = 0; r < numberOfThreads; r++) {
                            if (t > 0) {
                                localCount[t] += allCount[r][t - 1];
                            }
                        }
                        localCount[t] += acumval;
                        acumval = localCount[t];
                        for (int i = 0; i < id; i++) {
                            localCount[t] += allCount[i][t];
                        }
                    }

                    barry.await();

                    for (int i = lo; i <= hi; i++) {
                        b[localCount[(a[i] >>> shift) & mask]++] = a[i];
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }

        /**
         * Finding max element in array
         *
         * @param a array
         * @return max element
         * @throws InterruptedException
         */
        int findMaxPar(int[] a) throws InterruptedException {
            ArrayList<Thread> threads = new ArrayList<>();
            int max = 1;
            int threadId = 0;
            int processors = Runtime.getRuntime().availableProcessors();
            // p = n/(k+1) rounding down  ( Number of parts )
            int maxNumberOfThreads = a.length / (1 + max);
            if (processors > maxNumberOfThreads) {
                processors = maxNumberOfThreads;
            }

            while (true) {
                int m = a.length % processors;
                if (m == 0 || m >= 1) {
                    break;
                }
                processors--;
            }

            int elementsToSort = (int) Math.ceil((double) a.length / processors);
            Monitor.setFinalMax(processors * max);

            for (int i = 0; i < a.length; i = i + elementsToSort) {
                if (a.length < i + elementsToSort) {
                    elementsToSort = a.length - i;
                }
                Thread t = new Thread(new ThreadMax(threadId++, i, i + elementsToSort - 1, a,
                        max));
                threads.add(t);
                t.start();
            }
            //Waiting for the threads to finish
            for (Thread h : threads) {
                try {
                    h.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int[] sortedArray = Monitor.sorted;
            return sortedArray[0];
        }
    }


    public static class ThreadMax implements Runnable {
        private int id;
        private int v;
        private int h;
        private int[] a;
        private int number;

        public ThreadMax(int id, int v, int h, int[] a, int number) {
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

    public static class Monitor {

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
         *
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


    /***********************************************************
     * Oblig 3 - sekvensiell kode, INF2440 v2017.
     *            Ifi, Uio, Arne Maus
     * for store verdier av n > 100 m, kjør (f.eks):
     *     >java -Xmx16000m MultiRadix 1000000000
     ************************************************************/

    public static class MultiRadix {
        int n;
        int[] a;
        final static int NUM_BIT = 7; // alle tall 6-11 .. finn ut hvilken verdi som er best

        /**
         * Filling in the array with random numbers
         *
         * @param len length of the array
         */
        void doIt(int len) {
            a = new int[len];
            Random r = new Random(123);
            for (int i = 0; i < len; i++) {
                a[i] = r.nextInt(len);
            }
            a = radixMulti(a);
        }

        int[] radixMulti(int[] a) {
            long startTime = System.nanoTime();
            // 1-5 digit radixSort of : a[]
            int max = a[0], numBit = 2, numDigits, n = a.length;
            int[] bit;

            for (int i = 1; i < n; i++)
                if (a[i] > max) max = a[i];

            while (max >= (1L << numBit)) numBit++; // number of digits in binary max

            // bestem antall bit i numBits sifre
            numDigits = Math.max(1, numBit / NUM_BIT);
            bit = new int[numDigits];
            int rest = (numBit % numDigits), sum = 0;


            // fordel bitene vi skal sortere paa jevnt
            for (int i = 0; i < bit.length; i++) {
                bit[i] = numBit / numDigits;
                if (rest-- > 0) bit[i]++;
            }

            int[] t = a, b = new int[n];

            for (int i = 0; i < bit.length; i++) {
                radixSort(a, b, bit[i], sum);    // i-te siffer fra a[] til b[]
                sum += bit[i];
                // swap arrays (pointers only)
                t = a;
                a = b;
                b = t;
            }
            if (bit.length % 2 != 0) {
                // et odde antall sifre, kopier innhold tilbake til original a[] (nå b)
                System.arraycopy(a, 0, b, 0, a.length);
            }

            double tid = (System.nanoTime() - startTime) / 1000000.0;
            seqTime.add(tid);
            System.out.println("Sorted  " + n + " numbers sequentially:" + tid + "ms.");
            testSort(a);
            return a;
        } // end radixMulti

        /**
         * Sort a[] on one digit ; number of bits = maskLen, shiftet up 'shift' bits
         */
        void radixSort(int[] a, int[] b, int maskLen, int shift) {
            System.out.println("  \u2022 radixSort maskLen: " + maskLen + ", shift: " + shift);
            int acumVal = 0, j, n = a.length;
            int mask = (1 << maskLen) - 1;
            int[] count = new int[mask + 1];

            // b) count=the frequency of each radix value in a
            for (int i = 0; i < n; i++) {
                count[(a[i] >>> shift) & mask]++;
            }

            // c) Add up in 'count' - accumulated values
            for (int i = 0; i <= mask; i++) {
                j = count[i];
                count[i] = acumVal;
                acumVal += j;
            }
            // d) move numbers in sorted order a to b
            for (int i = 0; i < n; i++) {
                b[count[(a[i] >>> shift) & mask]++] = a[i];
            }

        }// end radixSort

        void testSort(int[] a) {
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] > a[i + 1]) {
                    System.out.println("Sorting error on place: " + i + " a[" + i + "]:" + a[i] + " > a[" + (i + 1) + "]:" + a[i + 1]);
                    return;
                }
            }
        }
    }
}
