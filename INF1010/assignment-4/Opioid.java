public abstract class Opioid extends Drug {
  private int strength;

  /**
  *Constructor
  *@param strength Shows how strong the opioid drug is
  *@param identificationNumber Unique number
  *@param name Name of the drug
  *@param price Price of the drug
  */
  Opioid (int strength,int identificationNumber,String name, double price) {
    super(identificationNumber,name,price);
    this.strength = strength;
  }

  /**
  *Sets how strong the opioid drug is
  *@param strength Shows how strong the opioid drug is
  */
  public void setStrength(int strength) {
    this.strength = strength;
  }

  /**
  *Returns how strong the opioid drug is
  */
  public int getStrength() {
    return strength;
  }

}
