//Oblig 5, oppgave 5.3-5.4
public class Oppgave54 {
  public static void main(String[] args) {
    Isbod isbod = new Isbod();
    isbod.ansett("Marta"); //hiring Marta
    isbod.ansett("Brage"); //hiring Brage
    isbod.ansett("Donald"); //hiring Donald
    isbod.printAlleAnsatte(); //Printing the names to the console
    isbod.giSistemannSparken();//Donald is fired
    isbod.printAlleAnsatte(); //Printing the rest of the people
  }
}
