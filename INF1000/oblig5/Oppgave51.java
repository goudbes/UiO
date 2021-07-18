//oblig 5 Oppgave 5.1
public class Oppgave51 {
  public static void main (String[] args) {
    Bil bil = new Bil();
    bil.kjorTur(20); //driving 20 km
    bil.kjorTur(20); // same..
    bil.kjorTur(700); // oops.. too much?
    System.out.println("The car has been driving " +bil.hentKilometerstand()+ " km."); //how long the car has been driving

  }
}
