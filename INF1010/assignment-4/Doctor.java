public class Doctor extends Person implements Equals, Comparable<Doctor> {

private  EnkelReseptListe docPrescription = new EldsteForstReseptListe();

public EnkelReseptListe getListe() {
  return docPrescription;
}
  /**
  *Constructor
  *@param name Name of the doctor
  */
  Doctor(String name) {
    super(name);
  }

  /**
  *Checks if the name equals to the object's name
  *@param name The name
  */
  public boolean same(String name) {
    return this.getName().equalsIgnoreCase(name);
  }

  /**
  *compareTo method for Doctor
  *@param doctor Doctor
  */
  public int compareTo(Doctor doctor) {
    return doctor.getName().compareTo(getName());
  }

  /**
  *toString method for doctor
  */
  public String toString() {
    return this.getName();
  }

  public void info() {
      System.out.println(this.getName() + ", " + "0");
  }
}
