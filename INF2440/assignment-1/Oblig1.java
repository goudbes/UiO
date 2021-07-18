import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Oblig1 {

    //Array for storing the threads
    private static ArrayList<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.out.println("Invalid parameters: <total number of answers> <number of relevant results>");
            System.exit(0);
        }

        int numberOfAnswers = Integer.parseInt(args[0]);
        int numberOfRelevantResults = Integer.parseInt(args[1]);

        if (numberOfAnswers <= numberOfRelevantResults || numberOfAnswers <= 0 || numberOfRelevantResults <= 0) {
            System.out.println("Validate the input data");
            System.exit(0);
        }

        int[] relevanceScore = new int[numberOfAnswers];
        Random random = new Random();

        //Filling the relevance score for each answer
        for (int i = 0; i < relevanceScore.length; i++) {
            relevanceScore[i] = random.nextInt(numberOfAnswers);
        }


        multithreadedSorting(relevanceScore, numberOfRelevantResults);
        sequentialSorting(relevanceScore, numberOfRelevantResults);
    }

    /**
     * Finding the largest k elements in the array
     *
     * @param a                        input array
     * @param numberOfRelevanceResults Number of relevant results
     * @return sorted array
     */
    public static int[] finnMax(int[] a, int numberOfRelevanceResults) {
        //Sorting a[0..k-1]
        int tmp;
        insertionSort(a, 0, numberOfRelevanceResults - 1);
        for (int j = numberOfRelevanceResults; j < a.length; j++) {
            if (a[numberOfRelevanceResults - 1] < a[j]) {
                tmp = a[j];
                a[j] = a[numberOfRelevanceResults - 1];
                a[numberOfRelevanceResults - 1] = tmp;
                insertSortOneElement(a, 0, numberOfRelevanceResults - 1);
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
    public static void insertionSort(int[] a, int v, int h) {
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
     * Inserting the rightmost element into already sorted part of the array
     *
     * @param a array
     * @param v lower bound of the array
     * @param h upper bound of the array
     */
    public static void insertSortOneElement(int[] a, int v, int h) {
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
     * Multithreaded sorting
     *
     * @param relevanceScore          array with score
     * @param numberOfRelevantResults number of relevant answers
     */
    public static void multithreadedSorting(int[] relevanceScore, int numberOfRelevantResults) {

        int processors = Runtime.getRuntime().availableProcessors();

        int threadId = 0;

        // p = n/(k+1) rounding down  ( Number of parts )
        int maxNumberOfThreads = relevanceScore.length / (numberOfRelevantResults + 1);
        if (processors > maxNumberOfThreads) {
            processors = maxNumberOfThreads;
        }

        while (true) {
            int m = relevanceScore.length % processors;
            if (m == 0 || m >= numberOfRelevantResults) {
                break;
            }
            processors--;
        }


        int elementsToSort = (int) Math.ceil((double) relevanceScore.length / processors);


        long startTime = System.nanoTime();
        Monitor.setFinalMax(processors * numberOfRelevantResults);

        for (int i = 0; i < relevanceScore.length; i = i + elementsToSort) {
            if (relevanceScore.length < i + elementsToSort) {
                elementsToSort = relevanceScore.length - i;
            }

            Thread t = new Thread(new SortingThread(threadId++, i, i + elementsToSort - 1, relevanceScore,
                    numberOfRelevantResults));

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

        //getting the final array from Monitor

        int[] sortedArray = Monitor.sorted;


        long endTime = System.nanoTime();
        double time = (endTime - startTime) / 1000000.0;
        System.out.println("Number of cores: " + Runtime.getRuntime().availableProcessors() + " Number of threads: " +
                processors);
        System.out.println("Max multithreaded solution: " + sortedArray[0] + " Time: " + time + " ms" +
                " ,nanosek/n: " + (endTime-startTime)/relevanceScore.length);

        startTime = System.nanoTime();
        Arrays.sort(relevanceScore);
        endTime = System.nanoTime();
        time = (endTime - startTime) / 1000000.0;
        System.out.println("Max Arrays.sort()  " + relevanceScore[relevanceScore.length - 1] + " Time: " + time + " ms" +
               " ,nanosek/n: " + (endTime-startTime)/relevanceScore.length);

        //To make sure that everything was sorted correctly
      /*  int i = 0;
        for (int j = 0; j < numberOfRelevantResults; j++) {
            if (sortedArray[j] != relevanceScore[relevanceScore.length - 1 - i]) {
                return false;
            }
            i++;
        }
        return true;*/
    }

    /**
     * Sequiential sorting
     *
     * @param a                       array with scores
     * @param numberOfRelevantResults number of relevant results
     */
    public static void sequentialSorting(int[] a, int numberOfRelevantResults) {

        int[] copy = new int[a.length];
        System.arraycopy(a, 0, copy, 0, a.length);

        long startTime = System.nanoTime();
        copy = finnMax(copy, numberOfRelevantResults);
        long endTime = System.nanoTime();
        double time = (endTime - startTime) / 1000000.0;

        System.out.println("Max sequential solution : " + copy[0] + " Time: " + time + " ms" +
                " ,nanosek/n: " + (endTime-startTime)/copy.length);
        //To check that sorting was done correctly
       /* if (validateResults(copy, a, numberOfRelevantResults)) {
            System.out.println("Sequential test passed");
        } else {
            System.out.println("Sequential test failed");
        }*/
    }

    public static boolean validateResults(int[] a, int[] b, int numberOfRelevantResults) {
        Arrays.sort(b);
        int i = 0;
        for (int j = 0; j < numberOfRelevantResults; j++) {
            if (a[j] != b[b.length - 1 - i]) {
                return false;
            }
            i++;
        }
        return true;
    }
}
