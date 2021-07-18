/***********************************************
Obligatorisk oppgave 6, inf1000. Deloppgave 6.1
************************************************/
public class Deloppgave61 {
  public static void main(String[] args) throws Exception {
    //Creating two objects
    Ord foersteOrd = new Ord("love");
    Ord andreOrd = new Ord("cookies");
    //Increasing count for the first word
    foersteOrd.oekAntall();
    //Printing out 2 words and their count
    System.out.println("The first word: " + foersteOrd.toString());
    System.out.println("The second word: " + andreOrd.toString());
    System.out.println("The number of " + foersteOrd.toString() + " is: " + foersteOrd.hentAntall());
    System.out.println("The number of " + andreOrd.toString() + " is: " + andreOrd.hentAntall());
  }
}
