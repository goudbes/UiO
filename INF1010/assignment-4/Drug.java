public abstract class Drug {
  private String name;
  private int identificationNumber;
  private double price;

  /**
  *Constructor
  * @param identificationNumber Unique number
  * @param name Name of the drug
  * @param price Price of the drug
  */
  Drug(int identificationNumber,String name, double price) {
    this.name = name;
    this.identificationNumber = identificationNumber;
    this.price = price;
  }

  /**
  *Sets the name of the drug
  *@param name Name of the drug
  */
  public void setName(String name) {
    this.name = name;
  }

  /**
  *Returns the name of the drug
  */
  public String getName() {
    return name;
  }

  /**
  *Sets unique number for the drug
  *@param identificationNumber Unique number of the drug
  */
  public void setIdentificationNumber(int identificationNumber) {
    this.identificationNumber = identificationNumber;
  }

  /**
  *Returns the unique number of the drug
  */
  public int getIdentificationNumber() {
    return identificationNumber;
  }

  /**
  *Sets the price for the drug
  *@param price The price of the drug
  */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
  *Returns the price of the drug
  */
  public double getPrice() {
    return price;
  }

  public boolean isTypeA() {
    return this instanceof Opioid;
  }

  public boolean isTypeB() {
    return this instanceof Addictive;
  }

  public boolean isTypeC() {
    return this instanceof Ordinary;
  }

  public boolean isMixture() {
    return this instanceof Mixture;
  }

  public boolean isPill() {
    return this instanceof Pill;
  }

  public void info() {
  //...
  }
}
