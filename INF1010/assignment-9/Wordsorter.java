import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.lang.System;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileDescriptor;
import java.util.Arrays;



public class Wordsorter {
  //Array with words read from file
  private static String[] words;
  //Number of the words in file
  private static int wordCnt;
  //Number of threads
  private static int threadCnt;
  //Array where we store all the threads
  private static ArrayList<Thread> threads = new ArrayList<Thread>();
  //Output file
  private static String outFile;
  //Final sorted and merged array
  private static String[] fin;

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    long startTime = System.currentTimeMillis();

    //Reading words from file and adding them to array
    words = readFile(args, words);
    //Finding out how many words each thread will sort
    int wordsToSort = wordCnt/threadCnt;
    //thread counter
    int t = 0;
    //Giving each thread it's part to sort, lower and higher indexes in array
    for (int i = 0; i<words.length; i= i + wordsToSort) {
      //Making sure that the last part of array doesn't fail
      if (words.length < i + wordsToSort) {
        wordsToSort = words.length - i;
      }
      //Creating a new thread
      Thread h = new Thread(new ThreadW(words,i,i+wordsToSort-1, t));
      //Adding thread to array with threads
      threads.add(h);
      //starting a thread
      h.start();
      t++;
    }

    //Making sure Monitor knows the length of final array
    Monitor.setFinalSize(wordCnt);
    //Waiting for the threads to die
    for(Thread h: threads){
      h.join();
    }

    //getting the final array from Monitor
    fin = Monitor.sorted;


    //Checking if the last element is null
    String s= fin[fin.length-1];
    if(s==null){
      throw new NullPointerException();
    }

    //Making a copy of final array, sorting it and then comparing those Arrays
    //to check if final array is sorted
    String[] test = Arrays.copyOf(fin,fin.length);
    Arrays.sort(test);
    if (!Arrays.equals(fin,test)) {
      System.out.println("Array isn't sorted.");
    }

    //writing to file
    writeToFile();

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("Total time in ms: " + totalTime);

  }

  /**
  *Reading from file and putting all the words into the array, which is returned
  *@param args arguments
  *@param words array with words
  */
  public static String[] readFile(String[] args,String[] words) throws FileNotFoundException{

    //Checking if we pass correct number of arguments
    if (args.length!=3) {
      throw new IllegalArgumentException("Wrong arguments.");
    }

    //First argument - number of threads
    try {
      threadCnt = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.out.println("Wrong argument: " + args[0]);
    }

    //Adding input file and output file names
    String inFile = args[1];
    outFile = args[2];

    //Creating a new file, throwing exception if it doesn't exist
    File file = new File(inFile);
    if (!file.exists()) {
      throw new FileNotFoundException("File doesn't exist");
    }

    //Starting to read the file.
    Scanner in = new Scanner(file);
    String line;
    int i = 0;

    line = in.nextLine();
    wordCnt = Integer.parseInt(line);
    //Initializing array with words
    words = new String[wordCnt];

    while (in.hasNextLine()) {
      if (i<wordCnt) {
        line = in.nextLine();
        words[i]=line;
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
    //returning the array with words
    return words;
  }

  /**
  *Prints the array to console
  *@param a Array
  */
  public static void printWords(String[] a) {
    for (int i=0; i<a.length;i++)
    System.out.println(a[i]);
  }

  /**
  *Writing to file
  */
  public static void writeToFile(){
    try{
      File file = new File(outFile);
      if(!file.exists()){
        file.createNewFile();
      }

      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);

      FileOutputStream f = new FileOutputStream(file);
      System.setOut(new PrintStream(f));

      printWords(Monitor.sorted);
      bw.close();
    }
    catch (IOException e){
      e.printStackTrace();
    } finally{
      System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
  }
}
