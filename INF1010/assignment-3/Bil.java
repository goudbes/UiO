public class Bil {
  private  String bilnummer;

  /**
  *Constructor
  *@param bilnummer Registration number
  *
  */
  Bil(String bilnummer) {
    this.bilnummer = bilnummer;
  }

  /**
  * Returns registration number
  */
  public String returnBilNummer() {
    return bilnummer;
  }
}
