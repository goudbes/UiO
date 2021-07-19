public class Patient extends Person {
  private int identificationNumber;
  private String nationalIdentityNumber;
  private String address;
  private String index;
  private EnkelReseptListe patPrescription = new YngsteForstReseptListe();

  public EnkelReseptListe getListe() {
    return patPrescription;
  }
  /**
  *Constructor
  *@param identificationNumber Unique number each patient has
  *@param name Patients name
  *@param nationalIdentityNumber Social security number/National identity number, 11 digits
  *@param address Patient's address including street and number
  *@param index 4-digits postal code
  */
  Patient(int identificationNumber,String name, String nationalIdentityNumber,
  String address, String index) {
    super(name);
    this.identificationNumber = identificationNumber;
    setNationalIdentityNumber(nationalIdentityNumber);
    this.address = address;
    setIndex(index);
  }

  /**
  *Sets identificationNumber
  *@param identificationNumber Unique number each patient has
  */
  public void setIdentificationNumber(int identificationNumber) {
    this.identificationNumber = identificationNumber;
  }

  /**
  *Returns identification number each patient has
  */
  public int getIdentificationNumber() {
    return identificationNumber;
  }

  /**
  *Sets 11 digits national identity number
  *@param nationalIdentityNumber National identity number
  */
  public void setNationalIdentityNumber(String nationalIdentityNumber) {
    if (nationalIdentityNumber.length() == 11) {
      this.nationalIdentityNumber = nationalIdentityNumber;
    } else {
      throw new IllegalArgumentException("Invalid National Identity number.");
    }
  }

  /**
  *Returns 11 digits national identity number
  */
  public String getNationalIdentityNumber() {
    return nationalIdentityNumber;
  }

  /**
  *Sets patient's address consisting of the street name and number
  *@param address Address, consists of street name and number
  */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
  *Returns patients address
  */
  public String getAddress() {
    return address;
  }

  /**
  *Sets 4 digits postal code
  *@param index Postal code
  */
  public void setIndex(String index) {
    if (index.length()==4) {
      this.index = index;
    } else {
      throw new IllegalArgumentException("Invalid postal code.");
    }
  }

  /**
  * Returns 4 digits postal code
  */
  public String getIndex() {
    return index;
  }
}
