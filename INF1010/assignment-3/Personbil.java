public class Personbil extends Fossilbil {
  private int antallPassengers;

  /**
  *Constructor
  *@param bilnummer Registration number
  *@param carbonDioxide The amount of CO2 the car emits
  *@param antallPassengers The number of passengers
  **/

  Personbil(String bilnummer, double carbonDioxide, int antallPassengers) {
    super(bilnummer,carbonDioxide);
    this.antallPassengers = antallPassengers;
  }

  /**
  * Returns the number of passengers
  */

  public int returnPassengers() {
    return antallPassengers;
  }
}
