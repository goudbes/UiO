import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AssignmentThree {
    //ANSI escape codes for formatting output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) throws FileNotFoundException {
        String needle;
        String haystack;

        if (args.length != 2) {
            throw new IllegalArgumentException("Verify input files.");
        }

        String needleFile = args[0];
        String haystackFile = args[1];
        needle = readInputFile(needleFile).replace("\n", "");
        haystack = readInputFile(haystackFile);

        System.out.println("Needle to be found: " + needle);

        if (needle.length() > haystack.length()) {
            throw new IllegalArgumentException("Needle is longer than the haystack.");
        }

        if (needle.length() == 0) {
            throw new IllegalArgumentException("Make sure the length of needle is >0");
        }

        if (haystack.length() == 0) {
            throw new IllegalArgumentException("Make sure the length of haystack is >0");
        }

        //Arraylist with found matches
        ArrayList<Integer> positions = BoyerMoore.search(needle, haystack);


        // print results
        if (positions.isEmpty()) {
            System.out.println("Needle wasn't found.");
        } else {
            System.out.println("\n");
            colorNeedle(haystack, positions, needle);
            System.out.println("\n");
            System.out.println("Found: ");
            for (Integer p : positions) {
                System.out.println("Match: " + haystack.substring(p, p + needle.length()) + " Position: " + p);
            }
        }
    }

    /**
     * Reading the file
     * @param filename Name of input file
     * @return Content of the input file
     * @throws FileNotFoundException
     */
    public static String readInputFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        String outputLine = "";
        if (file.exists() && !file.isDirectory()) {
            Scanner in = new Scanner(file);
            String line;
            while (in.hasNextLine()) {
                line = in.nextLine();
                if (line != null) {
                    outputLine += line;
                    outputLine += '\n';
                }
            }
            in.close();
        } else {
            throw new FileNotFoundException("File doesn't exist");
        }
        return outputLine.toLowerCase();
    }

    /**
     * Colorizing matches inside haystack
     * @param inputText haystack
     * @param positions Positions for each match
     * @param needle Needle
     */
    public static void colorNeedle(String inputText, ArrayList<Integer> positions, String needle) {
        int nLength = needle.length();
        int i = 0;
        while (i < inputText.length()) {
            if (positions.contains(i)) {
                for (int x = 0; x < nLength; x++) {
                    System.out.print(ANSI_RED + inputText.charAt(i) + ANSI_RESET);
                    i++;
                }
                continue;
            }
            System.out.print(inputText.charAt(i));
            i++;
        }
    }
}
