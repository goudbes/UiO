public class Fossilbil extends Bil{
  private double carbonDioxide;

  /**
  *Constructor
  *@param bilnummer Registration number
  *@param carbonDioxide The amount of carbon dioxide car emits
  */
  Fossilbil(String bilnummer, double carbonDioxide) {
    super(bilnummer);
    this.carbonDioxide = carbonDioxide;
  }

  /**
  *Returns the amount of carbon dioxide
  */
  public double returnCarbonDioxide() {
    return carbonDioxide;
  }
}
