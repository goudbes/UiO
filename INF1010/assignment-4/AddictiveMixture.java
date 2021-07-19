public class AddictiveMixture extends Addictive implements Mixture  {
  private double volume;
  private double activeSubstance;

  /**
  *Constructor
  *@param addictive Shows how strong the addictive drug is
  *@param identificationNumber Unique number
  *@param name Name of the drug
  *@param price Price of the drug
  *@param volume The volume of the bottle
  *@param activeSubstance Amount of active substance in 1 cubic centimetre
  */
  AddictiveMixture(String name, int identificationNumber, double price, int addictive,
  double volume, double activeSubstance) {
    super(addictive,identificationNumber,name,price);
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
    ", mikstur" + ", b, " + this.getPrice() +", " + this.getVolume() + ", " +
    this.getActiveSubstance() + ", " + this.getAddictive());
  }

}
