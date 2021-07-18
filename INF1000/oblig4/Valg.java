//Oppgave 4.4 VALG
import java.util.Scanner;
import java.io.File;
class Valg {
  public static void main(String[] args) throws Exception {
    Scanner input = new Scanner(new File("stemmer.txt"));
    String[] valg = new String[456];

    while (input.hasNextLine()) { //reading from file and saving votes in array
      for (int i=0; i<valg.length; i++) {
        valg[i] = input.nextLine();
      }
    }
    int teller = 0;
    int[] parties = new int[4]; // 0 AP 1 KrF 2 Sp 3 H  Counting the votes for each party
    for (int i= 0; i<valg.length; i++) {
      if (valg[i].contains("Ap")) {
        parties[0] = parties[0] + 1;
      } else if (valg[i].contains("KrF")) {
        parties[1] = parties[1] + 1;
      } else if (valg[i].contains("Sp")) {
        parties[2] = parties[2] + 1;
      } else if (valg[i].contains("H")) {
        parties[3] = parties[3] + 1;
      }
    }

    //  System.out.println(parties[0]);
    //  System.out.println(parties[1]);
    //  System.out.println(parties[2]);
    //  System.out.println(parties[3]);


    double pros = 456.0/100.0; //calculating the percentage of votes
    double ap = parties[0]/pros;
    double krf = parties[1]/pros;
    double sp = parties[2]/pros;
    double hoyre = parties[3]/pros;

    //  System.out.println("AP: " + String.format("%.1f",ap));
    //  System.out.println("KrF: " + String.format("%.1f",krf));
    //  System.out.println("Sp: "+ String.format("%.1f",sp));
    //  System.out.println("H: " + String.format("%.1f",hoyre));

    System.out.println("And the winner is AP with " + String.format("%.1f",ap) + "% votes"); //announcing the winner

  }
}
