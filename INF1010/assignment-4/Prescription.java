public class Prescription {
  private int identificationNumber;
  private Drug drug;
  private int patientsNum;
  private String doctorsName;
  private int reit;
  //Prescription is valid if the reit is more than 0
  private boolean valid = true;
  //Prescription is blue if the drug is free
  private boolean blue = false;



  /**
  *Constructor
  *@param identificationNumber - unique number prescription has
  *@param drug Pointer to the drug that was prescribed
  *@param doctorsName Name of the doctor that prescribed the drug
  *@param patientsNum Patients id number
  *@param reit Number of times the patient can get the drug
  */
  Prescription(int identificationNumber,Drug drug,String doctorsName,
  int patientsNum, int reit, String type) {
    this.identificationNumber = identificationNumber;
    this.drug = drug;
    this.doctorsName = doctorsName;
    this.patientsNum = patientsNum;
    setReit(reit);
    if (type.equals("blaa")) {
      blue = true;
    }
  }

  /**
  *Sets unique prescription number
  *@param identificationNumber Unique identificationNumber
  */
  public void setIdentificationNumber(int identificationNumber) {
    this.identificationNumber = identificationNumber;
  }

  /**
  *Returns unique prescription number
  */
  public int getIdentificationNumber() {
    return identificationNumber;
  }

  /**
  *Sets drug
  *@param drug Drug
  */
  public void setDrug(Drug drug) {
    this.drug = drug;
  }

  /**
  *Returns the drug
  */
  public Drug getDrug() {
    return drug;
  }

  /**
  *Sets doctor's Name
  *@param doctorsName Doctor's name
  */
  public void setDoctorsName(String doctorsName) {
    this.doctorsName = doctorsName;
  }

  /**
  *Returns doctor's name
  */
  public String getDoctorsName() {
     return doctorsName;
   }

   /**
   *Sets patient's name
   *@param patientsName Patient's name
   */
   public void setPatientsNum(int patientsNum) {
     this.patientsNum = patientsNum;
   }

   /**
   *Returns patient's name
   */
   public int getPatientsNum() {
     return patientsNum;
   }

   /**
   *Sets number of times the patient can get his drug. If reit equals 0,
   * prescription is invalid
   *@param reit Number of times the patient can get his drug
   */
   public void setReit(int reit) {
     this.reit = reit;
     valid = (reit != 0);
  }

   /**
   *Returns number of times the patient can get his drug
   */
   public int getReit() {
     return reit;
   }


  /**
  *Returns true if prescription is valid
  */
  public boolean isValid() {
    return valid;
  }

  /**
  * Returns true if prescription is "Blue"
  */
  public boolean isBlue() {
    return blue;
  }

  public void info() {
    if (isBlue()) {
      System.out.println(this.getIdentificationNumber() + ", blaa, " +
      this.getPatientsNum() + ", " + this.getDoctorsName() + ", " +
      this.getDrug().getIdentificationNumber() + ", " + this.getReit());
    } else {
      System.out.println(this.getIdentificationNumber() + ", hvit, " +
      this.getPatientsNum() + ", " + this.getDoctorsName() + ", " +
      this.getDrug().getIdentificationNumber() + ", " + this.getReit());
    }
  }


}
