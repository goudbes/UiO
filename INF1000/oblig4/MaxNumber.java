//Oppgave 4.5 Find the maximum value of all the numbers listen in file numbers.txt using a method that is finding
// maximum value
import java.util.Scanner;
import java.io.File;
class MaxNumber {
  public static void main (String[] args) throws Exception {
    Scanner input = new Scanner(new File("numbers.txt"));
    int[] array = new int[6];
    int i = 0;
    while (input.hasNextInt()) { // filling the array with integers from the file
      array[i++] = input.nextInt();
    }

    for (i=0; i<array.length;i++) { // printing out the array with numbers
      System.out.println(": " + array[i]);
    }

    System.out.println("Maximum number is: " + max(array)); //calling the method

  }

  public static int max (int[] array) { //method that is finding the maximum element in array
    int maximum = array[0];
    int position = 0;
    for (int i=0; i<array.length;i++){
      if (array[i]>maximum) {
        maximum = array[i];
      }
    }
    return maximum;
  }
}
