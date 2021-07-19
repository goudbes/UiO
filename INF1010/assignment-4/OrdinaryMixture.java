public class OrdinaryMixture extends Ordinary implements Mixture  {
  private double volume;
  private double activeSubstance;

  /**
  *Constructor
  *@param identificationNumber Unique number
  *@param name Name of the drug
  *@param price Price of the drug
  *@param volume The volume of the bottle
  *@param activeSubstance Amount of active substance in 1 cubic centimetre
  */
  OrdinaryMixture(String name, int identificationNumber, double price,
  double volume, double activeSubstance) {
    super(identificationNumber,name,price);
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
    ", mikstur" + ", c, " + this.getPrice() +", " + this.getVolume() + ", " +
    this.getActiveSubstance());
  }

}
