import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Oblig2 {

    public static ArrayList<Double> seqTime = new ArrayList<>();
    public static ArrayList<Double> parTime = new ArrayList<>();
    public static ArrayList<Double> seqTimeFact = new ArrayList<>();
    public static ArrayList<Double> parTimeFact  = new ArrayList<>();

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Invalid parameters, please enter N > 16");
            System.exit(0);
        }

        int maxNum = Integer.parseInt(args[0]);

        if (maxNum <= 16) {
            System.out.println("Please enter N > 16");
            System.exit(0);
        }

        int numberRun = 5;

        for(int i = 0; i < numberRun; i++) {
            System.out.println("\nSequential version");
            System.out.println("------------------");
            EratosthenesSil eratosthenesSil = new EratosthenesSil(maxNum);
            eratosthenesSil.printFact();
            //eratosthenesSil.printAllPrimes();
            System.out.println("\nParallel version");
            System.out.println("----------------");
            EratosthenesSilP eratosthenesSilP = new EratosthenesSilP(maxNum);
            eratosthenesSilP.printFact();
            //eratosthenesSilP.printAllPrimes();
        }

        for (int i = 0; i < numberRun; i++) {
            System.out.println(" ");
            System.out.println("Kjøring #" + i + " maxNum = " + maxNum);
            System.out.println("Primes seq." + seqTime.get(i));
            System.out.println("Primes par." + parTime.get(i));
            System.out.println("Fact seq." + seqTimeFact.get(i));
            System.out.println("Fact par." + parTimeFact.get(i));
        }

        System.out.println(" ");
        System.out.println("Primes seq. median " + median(seqTime));
        System.out.println("Primes par. median " + median(parTime));
        System.out.println("Primes speed up " + median(seqTime)/median(parTime));
        System.out.println("Fact seq. median " + median(seqTimeFact));
        System.out.println("Fact par. median " + median(parTimeFact));
        System.out.println("Fact speed up " + median(seqTimeFact)/median(parTimeFact));
        System.out.println(" ");
    }



    public static class EratosthenesSil {
        final int[] bitMask = {1, 2, 4, 8, 16, 32, 64};  // kanskje trenger du denne
        final int[] bitMask2 = {255 - 1, 255 - 2, 255 - 4, 255 - 8, 255 - 16, 255 - 32, 255 - 64}; // kanskje trenger du denne
        byte[] bitArr;           // bitArr[0] represents the 7 integers:  1,3,5,...,13, and so on
        int maxNum;              // all primes in this bit-array is <= maxNum


        EratosthenesSil(int maxNum) {
            this.maxNum = maxNum;
            bitArr = new byte[(maxNum / 14) + 1];
            //setter alle byte til 1
            setAllPrime();
            double startTime = System.nanoTime();
            generatePrimesByEratosthenes();
            double endTime = System.nanoTime();
            double msNum = ((endTime - startTime) / 1000000.0) / maxNum;
            seqTime.add((endTime - startTime) / 1000000.0);
            System.out.println("Generated all prime numbers <= " + maxNum + " in " + ((endTime - startTime) / 1000000.0) + "ms");
            System.out.print("With Eratosthenes sieve (ms/prime number): ");
            System.out.printf("%.9f\n", msNum);

        } // end konstruktor ErathostenesSil

        /**
         * Checks if the number is even
         *
         * @param num given number
         * @return true if even, false otherwise
         */
        public static boolean isEven(int num) {
            return (num & 1) == 0;
        }

        /**
         * Setting all the numbers as primes
         */
        void setAllPrime() {
            for (int i = 0; i < bitArr.length; i++) {
                bitArr[i] = (byte) 127;
            }
        }

        /**
         * Crossing out the number, which is not prime
         *
         * @param i the number to cross
         */
        void crossOut(int i) {
            //a &= b; is equivalent to a = a & b;
            bitArr[(i / 14)] &= bitMask2[(i % 14) >> 1];
        }

        /**
         * Checking if the number is prime
         *
         * @param i number to check
         * @return true if prime, false otherwise
         */
        boolean isPrime(int i) {
            if (i < 2) return false;
            if (i == 2) return true;
            if ((i % 2) == 0) return false;
            return (bitArr[i / 14] & bitMask[(i % 14) >> 1]) != 0;
        }

        /**
         * Printing out all the prime factors of the last 100 numbers t < N*N
         */
        public void printFact() {
            int i = 2;
            long maxNumProd = (long) maxNum * (long) maxNum;
            long number = maxNumProd - 1;
            double startTime = System.nanoTime();

            System.out.println(" ");
            while (number >= maxNumProd - 100L) {
                ArrayList<Long> factNumbers = factorize(number);
                int itemCount = factNumbers.size();
                System.out.print(number + " = ");
                for (long f : factNumbers) {
                    System.out.print(f);
                    itemCount--;
                    if (itemCount != 0) {
                        System.out.print("*");
                    }
                }
                System.out.println(" ");
                number = maxNumProd - (i++);
            }
            double endTime = System.nanoTime();
            double msNum = ((endTime - startTime) / 1000000.0) / 100;
            seqTimeFact.add((endTime - startTime) / 1000000.0);
            System.out.println("100 factorizations with print out took: " + ((endTime - startTime) / 1000000.0) + "ms");
            System.out.print("That is: ");
            System.out.printf("%.9f", msNum);
        }

        /**
         * Finding all prime factors for the given number
         *
         * @param num number
         * @return arraylist with prime factors
         */
        ArrayList<Long> factorize(long num) {
            ArrayList<Long> fakt = new ArrayList<>();

            //check for M < N*N
            if (num >= (long) maxNum * (long) maxNum) {
                return null;
            }

            for (int p = 2; p < Math.sqrt(num); p = nextPrime(p)) {
                while (num % p == 0) {
                    fakt.add((long) p);
                    num = num / p;
                }
            }

            //num is prime itself
            if (num > 1)
                fakt.add(num);

            return fakt;
        } // end factorize

        /**
         * Finding the next prime number after the given number
         *
         * @param i given number
         * @return next prime number
         */
        int nextPrime(int i) {
            if (isEven(i)) {
                i += 1;
            } else {
                i += 2;
            }
            while (i <= maxNum) {
                if (isPrime(i)) break;
                i += 2;
            }
            return i;
        }

        /**
         * Prints all prime numbers to the console
         */
        void printAllPrimes() {
            int count = 0;
            for (int i = 2; i <= maxNum; i++)
                if (isPrime(i)) {
                    count++;
                    System.out.println(i);
                }
            System.out.println("Count: " + count);
        }

        /**
         * Generating all the prime numbers
         */
        void generatePrimesByEratosthenes() {
            // krysser av alle  oddetall i 'bitArr[]' som ikke er primtall (setter de =0)
            crossOut(1);      // 1 is not a prime
            int crossOutNum;
            for (int i = 3; i <= Math.sqrt(maxNum); i = nextPrime(i)) {
                int j = 0;
                crossOutNum = 0;
                while (crossOutNum <= maxNum) {
                    crossOutNum = i * i + j * i;
                    if (crossOutNum <= maxNum)
                        crossOut(crossOutNum);
                    j += 2;
                }
            }
        }
    } // end class Bool

    public static class EratosthenesSilP {
        final int[] bitMask = {1, 2, 4, 8, 16, 32, 64};
        final int[] bitMask2 = {255 - 1, 255 - 2, 255 - 4, 255 - 8, 255 - 16, 255 - 32, 255 - 64};

        // One bitarray per thread ( main thread + processors )
        // to avoid slow synchronization
        byte[][] bitArr = new byte[Runtime.getRuntime().availableProcessors() + 1][];

        // Id per thread - to write in the separate bit arrays
        // However this was not optimal, the calls to tid.get() were expensive.
        AtomicInteger tidCount = new AtomicInteger();
        ThreadLocal<Integer> tid = new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return tidCount.getAndIncrement();
            }
        };

        int maxNum;

        EratosthenesSilP(int maxNum) {
            this.maxNum = maxNum;
            for (int i = 0; i < bitArr.length; i++) {
                bitArr[i] = new byte[(maxNum / 14) + 1];
            }
            //setter alle byte til 1
            setAllPrime();
            double startTime = System.nanoTime();
            generatePrimesByEratosthenes();
            double endTime = System.nanoTime();
            double msNum = ((endTime - startTime) / 1000000.0) / maxNum;
            parTime.add((endTime - startTime) / 1000000.0);
            System.out.println("Generated all prime numbers <= " + maxNum + " in " + ((endTime - startTime) / 1000000.0) + "ms");
            System.out.print("With Eratosthenes sieve (ms/prime number): ");
            System.out.printf("%.9f\n", msNum);
        }

        /**
         * Checks if the number is even
         *
         * @param num given number
         * @return true if even, false otherwise
         */
        public static boolean isEven(int num) {
            return (num & 1) == 0;
        }

        /**
         * Setting all the numbers as primes
         */
        void setAllPrime() {
            for (int t = 0; t < bitArr.length; t++) {
                for (int i = 0; i < bitArr[0].length; i++) {
                    bitArr[t][i] = (byte) 127;
                }
            }
        }

        /**
         * Crossing out the number, which is not prime
         *
         * @param i the number to cross
         */
        void crossOut(int i) {
            //a &= b; is equivalent to a = a & b;
            bitArr[tid.get()][(i / 14)] &= bitMask2[(i % 14) >> 1];
        }

        /**
         * Checking if the number is prime
         *
         * @param i number to check
         * @return true if prime, false otherwise
         */
        boolean isPrime(int i) {
            if (i < 2) return false;
            if (i == 2) return true;
            if ((i % 2) == 0) return false;
            return (bitArr[0][i / 14] & bitMask[(i % 14) >> 1]) != 0;
        }

        /**
         * Printing out all the prime factors of the last 100 numbers t < N*N
         */
        public void printFact() {
            int i = 2;
            long maxNumProd = (long) maxNum * (long) maxNum;
            long number = maxNumProd - 1;
            double startTime = System.nanoTime();

            int np = Runtime.getRuntime().availableProcessors();
            ExecutorService pool = Executors.newFixedThreadPool(np);

            // Prime borders
            int n = (int) Math.sqrt(number);
            ArrayList<Integer> borders = new ArrayList<>();
            borders.add(2);
            for(int j=np; j>1; j--) {
                borders.add(nextPrime(n/j));
            }
            // Make sure last border is the end
            borders.add(n);

            System.out.println(" ");
            while (number >= maxNumProd - 100L) {
                Vector<Long> factNumbers = factorize(pool, number, borders);
                int itemCount = factNumbers.size();
                System.out.print(number + " = ");
                for (long f : factNumbers) {
                    System.out.print(f);
                    itemCount--;
                    if (itemCount != 0) {
                        System.out.print("*");
                    }
                }
                System.out.println(" ");
                number = maxNumProd - (i++);
            }
            pool.shutdown();
            try {
                pool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("ERROR");
            }

            double endTime = System.nanoTime();
            double msNum = ((endTime - startTime) / 1000000.0) / 100;
            parTimeFact.add((endTime - startTime) / 1000000.0);
            System.out.println("100 factorizations with print out took: " + ((endTime - startTime) / 1000000.0) + "ms");
            System.out.print("That is: ");
            System.out.printf("%.9f", msNum);
        }


        /**
         * Finding all prime factors for the given number
         *
         * @param pool pool with worker threads
         * @param num number
         * @param borders prime borders per thread
         * @return arraylist with prime factors
         */
        Vector<Long> factorize(ExecutorService pool, final long num, ArrayList<Integer> borders) {
            Vector<Long> fakt = new Vector<>();
            Vector<Future> futures = new Vector<>();

            int np = Runtime.getRuntime().availableProcessors();
            AtomicLong anum = new AtomicLong(num);

            for (int i = 0; i < np; i++) {
                final long s = borders.get(i);
                final long e = borders.get(i + 1);
                futures.add(pool.submit(() -> {
                    for (long p = s; p < Math.min(e, Math.sqrt(anum.get())); p = nextPrime((int) p)) {
                        while (anum.get() % p == 0) {
                            fakt.add(p);
                            anum.set(anum.get() / p);
                        }
                    }
                }));
            }

            for (Future f : futures) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            if(anum.get() > 1) {
                fakt.add(anum.get());
            }

            if (fakt.isEmpty()) {
                fakt.add(num);
            }
            return fakt;
        }

        /**
         * Finding the next prime number after the given number
         *
         * @param i given number
         * @return next prime number
         */

        int nextPrime(int i) {
            if (isEven(i)) {
                i += 1;
            } else {
                i += 2;
            }
            while (i <= maxNum) {
                if (isPrime(i)) break;
                i += 2;
            }
            return i;
        }

        /**
         * Prints all prime numbers to the console
         */
        void printAllPrimes() {
            int count = 0;
            for (int i = 2; i <= maxNum; i++)
                if (isPrime(i)) {
                    System.out.println(i);
                    count++;
                }
            System.out.println("Count: " + count);
        }

        void crossOutAll(int i, int start, int stop) {
            int j = (start - i * i) / i;
            if (!isEven(j)) j--;
            if (j < 0) j = 0;
            int crossOutNum = 0;
            while (crossOutNum <= stop) {
                crossOutNum = i * i + j * i;
                if (crossOutNum <= stop)
                    crossOut(crossOutNum);
                j += 2;
            }
        }

        /**
         * Generating all the prime numbers
         */
        void generatePrimesByEratosthenes() {
            // krysser av alle  oddetall i 'bitArr[]' som ikke er primtall (setter de =0)
            crossOut(1);      // 1 is not a prime

            int r1 = (int) Math.ceil(Math.sqrt(Math.sqrt(maxNum)));
            int r2 = (int) Math.ceil(Math.sqrt(maxNum));

            // Precalculating smallest primes
            Vector<Integer> v = new Vector<>();
            for (int i = 3; i <= r1; i = nextPrime(i)) {
                crossOutAll(i, 0, r1);
                v.add(i);
            }

            // Multithreaded crossing of the medium range
            ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for (int i : v) {
                final int k = i;
                pool.submit(() -> crossOutAll(k, r1, r2));
            }

            pool.shutdown();

            try {
                pool.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("EЯЯOR");
            }

            for (int t = 0; t < bitArr.length; t++) {
                for (int i = 0; i < bitArr[0].length; i++) {
                    bitArr[0][i] &= bitArr[t][i];
                }
            }

            tidCount.set(1);

            // Multithreaded crossing of the full range
            pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for (int i = 3; i <= r2; i = nextPrime(i)) {
                final int k = i;
                pool.submit(() -> crossOutAll(k, r2, maxNum));
            }

            pool.shutdown();

            try {
                pool.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("EЯЯOR");
            }

            for (int t = 0; t < bitArr.length; t++) {
                for (int i = 0; i < bitArr[0].length; i++) {
                    bitArr[0][i] &= bitArr[t][i];
                }
            }
        }
    }

    /**
     * Returens the median of an arraylist
     * @param list input arraylist
     * @return median number
     */
    public static double median (ArrayList<Double> list) {
        Collections.sort(list);
        int mid = list.size() / 2;
        double median = list.get(mid);
        if (list.size() % 2 == 0) {
            median = (median + list.get(mid - 1)) / 2;
        }
        return median;
    }
}