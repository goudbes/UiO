import java.util.ArrayList;

public class BoyerMoore {

    private static int[] badCharShift = new int[256];     // the bad-character skip array
    private static String needle;      // Needle

    /**
     * Preprocesses the pattern string.
     * @param needle the pattern
     */
    public static void preprocess(String needle) {
        BoyerMoore.needle = needle;

        // position of rightmost occurrence of c in the pattern
        for (int c = 0; c < badCharShift.length; c++)
            badCharShift[c] = -1;
        for (int j = 0; j < needle.length(); j++) {
            badCharShift[needle.charAt(j)] = j;
        }
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param  needle Pattern to be matched
     * @param  haystack Input text
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
    public static ArrayList<Integer> search(String needle, String haystack) {
        preprocess(needle);
        int m = needle.length();
        int n = haystack.length();
        ArrayList<Integer> positions = new ArrayList<>();
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m-1; j >= 0; j--) {
                if (needle.charAt(j) == '_') {
                    continue;
                } else if (needle.charAt(j) != haystack.charAt(i+j)) { //if we've got a mismatch
                    skip = Math.max(1, j - badCharShift[haystack.charAt(i + j)]);
                    if(needle.substring(0,j).contains("_")){ // Should max skip to next _
                        for (int x = j; x >= 0; x--) {
                            if (needle.charAt(x) == '_' && ((j-x) < skip)) {
                                skip = j-x;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            if (skip == 0) {
                positions.add(i);
                skip = needle.length();
            }    // found, added to arraylist with positions
        }
        return positions;
    }
}
