public abstract class Addictive extends Drug {
  private int addictive;

  /**
  *Constructor
  *@param addictive Shows how strong the addictive drug is
  *@param identificationNumber Unique number
  *@param name Name of the drug
  *@param price Price of the drug
  */
  Addictive (int addictive,int identificationNumber,String name, double price) {
    super(identificationNumber,name,price);
    this.addictive = addictive;
  }

  /**
  *Sets how strong the addictive drug is
  *@param addictive Shows how strong the addictive drug is
  */
  public void setAddictive(int addictive) {
    this.addictive = addictive;
  }

  /**
  *Returns how strong the addictive drug is
  */
  public int getAddictive() {
    return addictive;
  }

}
