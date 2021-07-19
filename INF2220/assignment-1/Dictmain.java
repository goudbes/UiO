import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Dictmain {

  //ANSI escape codes for formatting output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  private static int numberOfNodes = 0;

  public static void main(String args[]) throws FileNotFoundException {
    //Creating a dictionary
    Dictionary dictionary = new Dictionary();
    //Reading words from the file..
    readFile("dictionary.txt",dictionary);
    //Removing the word busybody
    dictionary.delete("busybody");
    //System.out.println(dictionary.search("busybody"));
    // re-adding the word busybody
    dictionary.insert("busybody");
    //System.out.println(dictionary.search("busybody"));

    do {
      System.out.print(ANSI_PURPLE + "\nPlease type a word: " + ANSI_RESET);
      //  try {
      Scanner input = new Scanner(System.in);
      // Get user input from the keyboard
      String in = input.nextLine().toLowerCase();
      //Exiting program if q is typed
      if (in.equalsIgnoreCase("q")) {
        int maxDepth = dictionary.maxDepth(dictionary.getRoot());
        System.out.println("------------------------------------");
        System.out.println("Statistics");
        System.out.println(" ");
        System.out.println("Max depth of the binary tree is: " + maxDepth);
        System.out.println(" ");
        System.out.println("Nodes per each depth of the tree: ");
        System.out.println(" ");
        int[] counts = new int[dictionary.maxDepth(dictionary.getRoot())];
        int[] averageDepth = new int[dictionary.maxDepth(dictionary.getRoot())];

        dictionary.nodesPerDepth(dictionary.getRoot(),counts,0);
        for (int i = 0; i <counts.length;i++) {
          //sum of depth for each level
          averageDepth[i] = i*counts[i];
          System.out.println("Depth: " + i + " number of nodes: " + counts[i]);
        }

        int d = 0;
        for (int i = 0; i<averageDepth.length;i++) {
          d = d + averageDepth[i];
        }
        //System.out.println("Number of nodes: " + numberOfNodes);
        double averageDpth = d/numberOfNodes;
        System.out.println(" ");
        System.out.println("The average depth of all the nodes: " + averageDpth);
        System.out.println(" ");
        System.out.println("Alphabetically first word in the dictionary: " + dictionary.findLeftElement(dictionary.getRoot()));
        System.out.println("Alphabetically last word in the dictionary: " + dictionary.findRightElement(dictionary.getRoot()));
        System.exit(0);
      }

      //Searching for the word in dictionary..
      String word = dictionary.search(in);

      if (word!=null) {
        System.out.println(ANSI_GREEN + "The word was found: "+ ANSI_RESET +word);
      } else {
        System.out.println("The word wasn't found");
        System.out.println("------------------------------------");
        System.out.println("Similar words that exist in the dictionary");

        int countSimilarWords = 0;
        //Searching for similar words..
        long start = System.nanoTime();
        //searching for similarOne
        String[] simOne = similarOne(in);
        //searching for similarTwo
        String[] simTwo = similarTwo(in);
        //searching for similarThree
        String[] simThree = similarThree(in);
        //searching for similarFour
        String[] simFour = similarFour(in);

        long elapsedTime = System.nanoTime() - start;

        //Printing out similar words
        for (String s: simOne) {
          String simOneWord = dictionary.search(s);
          if (simOneWord!=null) {
            countSimilarWords++;
            System.out.println("* " + simOneWord);
          }
        }

        for (String s: simTwo) {
          String similarWord = dictionary.search(s);
          if (similarWord!=null) {
            countSimilarWords++;
            System.out.println("* " + similarWord);
          }
        }

        for (String s: simThree) {
          String similarWord = dictionary.search(s);
          if (similarWord!=null) {
            countSimilarWords++;
            System.out.println("* " + similarWord);
          }
        }

        for (String s: simFour) {
          String similarWord = dictionary.search(s);
          if (similarWord!=null) {
            countSimilarWords++;
            System.out.println("* " + similarWord);
          }
        }

        System.out.println(" ");
        System.out.println("Number of words found: " + countSimilarWords);
        System.out.println("Time used to generate similar words (nano): " + elapsedTime);
      }

      // } catch (Exception e) {
      //   System.out.println(ANSI_RED  + "Program failed due to: " + e +" " + ANSI_RESET);
      // }
    }
    while(true);
  }

  public static boolean readFile(String filename, Dictionary dictionary) throws FileNotFoundException {
    //Creating a new file, throwing exception if it doesn't exist
    File file = new File(filename);
    if (!file.exists()) {
      throw new FileNotFoundException("File doesn't exist");
    }

    //Starting to read the file.
    Scanner in = new Scanner(file);
    String line;
    int i = 0;

    int wordCnt = 75000;

    while (in.hasNextLine()) {
      if (i<wordCnt) {
        line = in.nextLine();
        dictionary.insert(line);
        numberOfNodes++;
        i++;
      } else {
        throw new IllegalArgumentException("Too many words");
      }
    }

    if (i<wordCnt) {
      throw new IllegalArgumentException("Too few words");
    }
    //Closing scanner
    in.close();
    return true;
  }

  /**
  * Removing character at defined position in a string
  * @param s String
  * @param pos Position in a string
  * @return new string
  * */
  public static String removeCharAt(String s, int pos) {
    return s.substring(0, pos) + s.substring(pos + 1);
  }

  /**
  * Generating words identical to the input word, except one letter
  * was added in front, in the end or in the middle of the word
  * @param word Input word
  * @return array of generated words
  */
  public static String[] similarFour(String word) {
    int length = word.length();
    String[] words = new String[length];
    int z = 0;
    char[] word_array = word.toCharArray();
    for (int i = 0; i < word_array.length; i++) {
      String s = removeCharAt(word,i);
      words[z++]=s;
    }
    return words;
  }

  /**
  * Generates words identical to the input, except one letter has been
  * removed.
  * @param word Input word
  * @return array of generated words
  */
  public static String[] similarThree(String word) {
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    String[] words = new String[52];
    int z = 0;
    char[] word_array = word.toCharArray();
    for (char c: alphabet) {
      words[z++] = new String( c+ word);
    }

    for (char c: alphabet) {
      words[z++] = new String(word+c);
    }
    return words;
  }

  /**
  * Generates words identical to the input, except one letter has been
  * replaced with another.
  * @param word Input word
  * @return array of generated words
  */
  public static String[] similarTwo(String word) {
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    int length = word.length();
    String[] words = new String[25*length];
    int z = 0;
    char[] word_array = word.toCharArray();

    for (int i = 0; i < word_array.length; i++) {
      for (char c: alphabet) {
        if (c==word.charAt(i)) {
          continue;
        }
        word_array[i]=c;
        words[z++]=new String(word_array);
      }
      word_array[i]=word.charAt(i);
    }
    return words;
  }

  /**
  * Generates words identical to the input, except that two letters next to each other
  * have been switched
  * @param word Input word
  * @return array of generated words
  */
  public static String[] similarOne(String word){
    char[] word_array = word.toCharArray();
    char[] tmp;
    String[] words = new String[word_array.length-1];
    for(int i = 0; i < word_array.length - 1; i++){
      tmp = word_array.clone();
      words[i] = swap(i, i+1, tmp);
    }
    return words;
  }

  /**
  * Swapping two characters
  * @param a position one
  * @param b position two
  * @param words array of characters
  * @return Returnes string
  */
  public static String swap(int a, int b, char[] word){
    char tmp = word[a];
    word[a] = word[b];
    word[b] = tmp;
    return new String(word);
  }


  // public static void findSimilarWords(String[] array, Dictionary dictionary) {
  //   for (String s: array) {
  //     String word = dictionary.search(s);
  //     if (s!=null) {
  //       System.out.println(s);
  //     }
  //   }
  // }
}
