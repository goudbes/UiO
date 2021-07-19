public class Elbil extends Bil {
  private int batteri;

  /**
  *Constructor
  *@param bilnummer Registration number
  *@param batteri Battery capacity, kW
  */
  Elbil(String bilnummer, int batteri) {
    super(bilnummer);
    this.batteri = batteri;
  }

  /**
  * Returns battery capacity
  */
  public int returnBatteri() {
    return batteri;
  }
}
