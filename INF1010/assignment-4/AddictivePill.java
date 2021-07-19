public class AddictivePill extends Addictive implements Pill {
  private int numberOfPills;
  private double activeSubstance;

  /**
  *Constructor
  *@param addictive Shows how strong the addictive drug is
  *@param identificationNumber Unique number
  *@param name Name of the drug
  *@param price Price of the drug
  *@param numberOfPills Number of pills
  *@param activeSubstance Amount of active substance in 1 pill
  */
  AddictivePill(String name, int identificationNumber, double price, int addictive,
  int numberOfPills, double activeSubstance) {
    super(addictive,identificationNumber,name,price);
    this.numberOfPills = numberOfPills;
    this.activeSubstance = activeSubstance;
  }

  /**
  *Sets number of pills
  *@param numberOfPills Number of pills in a box
  */
  public void setNumberOfPills(int numberOfPills) {
    this.numberOfPills = numberOfPills;
  }

  /**
  *Returns the number of pills
  */
  public int getNumberOfPills() {
    return numberOfPills;
  }

  /**
  *Sets the amount of active substance
  *@param activeSubstance Amount of active substance in 1 pill
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
    ", pille" + ", b, " + this.getPrice() +", " + this.getNumberOfPills() + ", " +
    this.getActiveSubstance() + ", " + this.getAddictive());
  }

}
