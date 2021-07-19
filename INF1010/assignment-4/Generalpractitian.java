public class Generalpractitian extends Doctor implements Agreement {
  private int agreementNumber;
  private EnkelReseptListe gpPrescription = new EldsteForstReseptListe();

  Generalpractitian(String name, int agreementNumber) {
    super(name);
    this.agreementNumber = agreementNumber;
  }

  /**
  *Sets agreement number
  *@param agreementNumber Agreement number
  */
  public void setAgreementNumber(int agreementNumber) {
    this.agreementNumber = agreementNumber;
  }

  /**
  *Returns agreement number
  */
  public int getAgreementNumber() {
    return agreementNumber;
  }

  @Override
  public void info() {
    System.out.println(this.getName() + ", " + this.getAgreementNumber());
  }
}
