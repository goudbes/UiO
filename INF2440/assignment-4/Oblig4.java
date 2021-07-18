import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.IntStream;

public class Oblig4 {

    public static ArrayList<Double> seqTime = new ArrayList<>();
    public static ArrayList<Double> parTime = new ArrayList<>();

    // Accessed by TegnUt
    static int MAX_Y = 10;
    static int MAX_X = 10;
    ArrayList<Integer> x;
    ArrayList<Integer> y;
    int n;

    private IntList allPoints;
    private IntList lines = new IntList();

    private Oblig4(int n) {
        this.n = n;
        NPunkter17 nPunkter17 = new NPunkter17(n);
        allPoints = nPunkter17.lagIntList();
        MAX_X = NPunkter17.maxXY;
        MAX_Y = NPunkter17.maxXY;
        x = new ArrayList<>();
        y = new ArrayList<>();
        nPunkter17.fyllArrayer(x, y);
    }

    int[] findMinMax(ArrayList<Integer> arr) {
        Collector<Integer, int[], int[]> minMaxCollector =
                Collector.of(
                        // Supplier
                        () -> new int[]{0, 0}, // min id, max id
                        // Accumulator
                        (res, id) -> {
                            if (arr.get(id) < arr.get(res[0])) {
                                res[0] = id;
                            }
                            if (arr.get(id) > arr.get(res[1])) {
                                res[1] = id;
                            }
                        },
                        // Combiner
                        (res1, res2) -> new int[]{
                                arr.get(res1[0]) < arr.get(res2[0]) ? res1[0] : res2[0],
                                arr.get(res1[1]) > arr.get(res2[1]) ? res1[1] : res2[1]
                        }
                );

        return IntStream.range(0, arr.size() - 1).boxed().collect(minMaxCollector);
    }

    class Cloudsplit {
        int maxP;
        int minP;
        ArrayList<Integer> over = new ArrayList<>();
        ArrayList<Integer> under = new ArrayList<>();
    }

    Cloudsplit findExtremePoint(int p1, int p2, List<Integer> p, List<Integer> result, boolean top) {
        // ax + by + c = 0
        // (y1-y2)x + (x2-x1)y + (y2*x1 - y1*x2)
        Cloudsplit cs = new Cloudsplit();
        List<Integer> zp = new ArrayList<>();
        int minD = Integer.MAX_VALUE;
        int maxD = Integer.MIN_VALUE;
        for (int i = 0; i < p.size(); i++) {
            int dist = ((y.get(p1) - y.get(p2)) * x.get(p.get(i)))
                    + ((x.get(p2) - x.get(p1)) * y.get(p.get(i)))
                    + ((y.get(p2) * x.get(p1)) - (y.get(p1) * x.get(p2)));
            if (dist > 0) {
                cs.over.add(p.get(i));
                if (dist > maxD) {
                    maxD = dist;
                    cs.maxP = p.get(i);
                }
            } else if (dist < 0) {
                cs.under.add(p.get(i));
                if (dist < minD) {
                    minD = dist;
                    cs.minP = p.get(i);
                }
            } else {
                // Points on the line that are not the start of end point
                if (!(p.get(i) == p1 || p.get(i) == p2))
                    zp.add(p.get(i));
            }
        }
        if (cs.over.isEmpty()) {
            // There were no points above so this is a line on the hull
            // therefore include the zero distance points in the result.
            // Annd sort them on the line.
            if(top) {
                zp.sort((Integer a, Integer b) -> x.get(a) > x.get(b) ? 1 : -1);
            } else {
                zp.sort((Integer a, Integer b) -> x.get(a) < x.get(b) ? 1 : -1);
            }

            result.addAll(zp);
        }
        return cs;
    }

    void calculate() {
        int[] minMax = findMinMax(x);

        ArrayList<Integer> xIndexes = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            xIndexes.add(i);
        }
        List<Integer> result = new ArrayList<>();
        Cloudsplit cs = findExtremePoint(minMax[0], minMax[1], xIndexes, result, true);

        result.add(minMax[0]);

        calculateRec(minMax[0], minMax[1], cs.maxP, cs.over, result, true);
        calculateRec(minMax[1], minMax[0], cs.minP, cs.under, result, false);

        for (int p : result) {
            lines.add(p);
            //System.out.println(p); //print result
        }
    }

    void calculateRec(int p1, int p2, int p3, List<Integer> checkPoints, List<Integer> result, boolean top) {

        if (checkPoints.isEmpty()) {
            result.add(p2);
            return;
        }

        Cloudsplit cs = findExtremePoint(p1, p3, checkPoints, result, top);
        calculateRec(p1, p3, cs.maxP, cs.over, result, top);

        cs = findExtremePoint(p3, p2, checkPoints, result, top);
        calculateRec(p3, p2, cs.maxP, cs.over, result, top);
    }

    void calculateRecP(int p1, int p2, int p3, List<Integer> checkPoints, List<Integer> result, int lvl, boolean top) {

        if (checkPoints.isEmpty()) {
            result.add(p2);
            return;
        }

        if (lvl < Math.sqrt(Runtime.getRuntime().availableProcessors())) {
            List<Integer> resultsLeft = new ArrayList<>();
            List<Integer> resultsRight = new ArrayList<>();
            Thread leftThread = new Thread(() -> {
                Cloudsplit cs = findExtremePoint(p1, p3, checkPoints, resultsLeft, top);
                calculateRecP(p1, p3, cs.maxP, cs.over, resultsLeft, lvl + 1, top);
            });
            Thread rightThread = new Thread(() -> {
                Cloudsplit cs = findExtremePoint(p3, p2, checkPoints, resultsRight, top);
                calculateRecP(p3, p2, cs.maxP, cs.over, resultsRight, lvl + 1, top);
            });
            leftThread.start();
            rightThread.start();
            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.addAll(resultsLeft);
            result.addAll(resultsRight);
        } else {
            Cloudsplit cs = findExtremePoint(p1, p3, checkPoints, result, top);
            calculateRecP(p1, p3, cs.maxP, cs.over, result, lvl + 1, top);

            cs = findExtremePoint(p3, p2, checkPoints, result, top);
            calculateRecP(p3, p2, cs.maxP, cs.over, result, lvl + 1, top);
        }
    }

    void tegnUt() {
        TegnUt tegnUt = new TegnUt(this, lines, "Oblig 4");
    }

    void calculateParallell() {
        int[] minMax = findMinMax(x);
        ArrayList<Integer> xIndexes = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            xIndexes.add(i);
        }

        List<Integer> resultsUp = new ArrayList<>();
        Cloudsplit cs = findExtremePoint(minMax[0], minMax[1], xIndexes, resultsUp, true);

        List<Integer> resultsDown = new ArrayList<>();
        resultsUp.add(minMax[0]);

        Thread upThread = new Thread(() -> calculateRecP(minMax[0], minMax[1], cs.maxP, cs.over, resultsUp, 1,true));
        Thread downThread = new Thread(() -> calculateRecP(minMax[1], minMax[0], cs.minP, cs.under, resultsDown, 1, false));

        upThread.start();
        downThread.start();
        try {
            upThread.join();
            downThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Merge results
        resultsUp.addAll(resultsDown);

        for (int p : resultsUp) {
            lines.add(p);
            //System.out.println(p); //print result
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid parameters.");
            System.exit(0);
        }

        int n = Integer.parseInt(args[0]);

        if (n < 3) {
            System.out.println("Enter n >= 3");
            System.exit(0);
        }

        int numberRun = 5;

        Oblig4 oblig = new Oblig4(n);

        double startTime;
        double duration;

        System.out.println("\nSequential solution: \n");

        for (int i = 0; i < numberRun; ++i) {
            startTime = System.nanoTime();
            oblig.calculate();
            duration = (System.nanoTime() - startTime) / 1000000.0;
            System.out.println("Run #" + (i + 1) + " took " + duration + " ms.");
            seqTime.add(duration);

        }
        System.out.println("Median time for sequential solution: " + median(seqTime));

        System.out.println("\nParallell solution: \n");

        for (int i = 0; i < numberRun; ++i) {
            startTime = System.nanoTime();
            oblig.calculateParallell();
            duration = (System.nanoTime() - startTime) / 1000000.0;
            System.out.println("Run #" + (i + 1) + " took " + duration + " ms.");
            parTime.add(duration);
        }
        System.out.println("Median time for parallell solution: " + median(parTime));
        System.out.println("\nSpeed up " + median(seqTime) / median(parTime));

        //oblig.tegnUt();
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
}
