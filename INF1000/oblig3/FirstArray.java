//oppgave 3.2 oblig 3
import java.util.Scanner;
public class FirstArray {
  public static void main(String[] args) {
    int[] anArray= {0,1,2,3}; //declaration of an array with 4 numbers
    String[] stringArray = new String[5]; //my string array
    Scanner input = new Scanner(System.in);
    int j = 0;
    int i = 0;
    int lastPosition = 0;
    //filling string-array with names
    System.out.println("Tast inn fem navn:");
    for (i = 0; i< stringArray.length; i++) {
      stringArray[i] = input.nextLine();
    }
    // b) Endre slik at tallene blir satt inn ved hjelp av en loekke.
    for (i = 0; i< anArray.length; i++) {
      anArray[i] = j;
      j++;
    }
    //getting the last position of my int array and c) Endre foerste og siste tallet i arrayen til aa vaere 1337.
    lastPosition = anArray.length-1;
    anArray[0] = 1337;
    anArray[lastPosition] = 1337;

    //Skriv ut innholdet i begge arrayer ved hjelp av while-loekker
    i = 0;
    System.out.println("\nInnhold i int-arrayen");
    while (i < anArray.length) {
      System.out.println(anArray[i]);
      i++;
    }

    i = 0;
    System.out.println("\nInnhold i String-arrayen");
    while (i < stringArray.length) {
      System.out.println(stringArray[i]);
      i++;
    }
  }
}
