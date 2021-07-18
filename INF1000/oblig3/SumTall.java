//Oblig 3, oppgave 3.1
import java.util.Scanner;
public class SumTall {
  public static void main (String[] args) {
    int sum = 0;
    Scanner input = new Scanner(System.in);
    System.out.println("Input numbers: ");
    while(input.hasNextInt()) { // waiting for the next token and to determine if it is an integer
      int number = input.nextInt(); //assigning a value from input to the variable number
      if (number == 0) { //if it gets 0, then we print out the sum and leave the loop
        System.out.println("Summen av tallene er: " + sum);
        break;
      } else {
        sum = sum + number; // counting the sum of the numbers
      }
    }
  }
}
