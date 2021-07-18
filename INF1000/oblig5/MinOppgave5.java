//Oppgave 5.5 Create a String array, ask user to fill it in with names. Create an object
//from a class that can sort and print sorted names to the console.
import java.util.Scanner;
public class MinOppgave5 {
  public static void main(String[] args) {
    String[] navn = new String[10];
    Scanner input = new Scanner(System.in);
    //asking user to write 9 names
    for (int i=0; i<navn.length;i++) {
      System.out.print("Write a name: ");
      navn[i] = input.nextLine();
    }
    //creating an object
    Names names = new Names();
    //calling a method of an object names
    names.sortAndPrint(navn);

  }
}
