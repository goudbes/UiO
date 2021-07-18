//Oppgave 4.1 Temperatur
import java.util.Scanner;
import java.io.File;
class Temperatur {
  public static void main(String[] args) throws Exception {
    Scanner input = new Scanner(new File("temperatur.txt"));
    int[] temperature = new int[12];
    int i = 0;

    while (input.hasNextInt()) {
      temperature[i++] = input.nextInt(); //getting the values from the file into array
    }

    //for (i=0; i< temperature.length; i++) {
    //System.out.println("temperature["+i+"]: " + temperature[i]);
    //}

    System.out.println("Gjennomsnittstemperaturen er: " + String.format("%.3f",gtemp(temperature))); //calling the method and printing the result to the console
  }

  public static Double gtemp (int[] temperature) { //method that calculates the average temperature with array as parameter
    Double temp = 0.0;
    for (int i=0; i<temperature.length;i++){
      temp = temp + temperature[i];
    }
    temp = temp/12;
    return temp;
  }

}
