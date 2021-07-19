public class OpioidMixture extends Opioid implements Mixture  {
  private double volume;
  private double activeSubstance;

  /**
  *Constructor
  *@param opioid Shows how strong the opioid drug is
  *@param identificationNumber Unique number
  *@param name Name of the drug
  *@param price Price of the drug
  *@param volume The volume of the bottle
  *@param activeSubstance Amount of active substance in 1 cubic centimetre
  */
  OpioidMixture(String name, int identificationNumber, double price, int opioid,
  double volume, double activeSubstance) {
    super(opioid,identificationNumber,name,price);
    this.volume = volume;
    this.activeSubstance = activeSubstance;
  }

  /**
  *Sets the volume of the bottle
  *@param volume The volume of the bottle
  */
  public void setVolume(double volume) {
    this.volume = volume;
  }

  /**
  *Returns the volume of the bottle
  */
  public double getVolume() {
    return volume;
  }

  /**
  *Sets the amount of active substance in 1 cubic centimetre
  *@param activeSubstance Amount of active substance in 1 cubic centimetre
  */
  public void setActiveSubstance(double activeSubstance) {
    this.activeSubstance = activeSubstance;
  }

  /**
  *Returns the amount of active substance
  */
  public double getActiveSubstance() {
    return activeSubstance;
  }

  public void info() {
    System.out.println(this.getIdentificationNumber() + ", " + this.getName() +
    ", mikstur" + ", a, " + this.getPrice() +", " + this.getVolume() + ", " +
    this.getActiveSubstance() + ", " + this.getStrength());
  }

}
