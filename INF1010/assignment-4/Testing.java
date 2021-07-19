import java.util.*;
public class Testing {
  public static void main (String[] args) {

    /**
    * Testing drugs
    */

    List<Drug> array = new ArrayList<Drug>();
    List<Person> arrayPerson = new ArrayList<Person>();

    OpioidMixture morphine_mix = new OpioidMixture ("Morphine Sulfate Oral Solution",
    0, 300, 3, 5, 20);
    OpioidPill magnus = new OpioidPill("Magnus MR", 1, 250, 3, 10, 30);

    AddictiveMixture cosylan = new AddictiveMixture("Cosylan",2,127.30,1,300,1.7);
    AddictivePill valium = new AddictivePill("Valium",3,461,2,28,10);

    OrdinaryMixture paracetamol = new OrdinaryMixture("Paracetamol",4,99.5,1000,1.22);
    OrdinaryPill paracet = new OrdinaryPill("Paracetamol ratiopharm",5,36,20,500);

    array.add(morphine_mix);
    array.add(magnus);
    array.add(cosylan);
    array.add(valium);
    array.add(paracetamol);
    array.add(paracet);

    for (Drug drug:array) {
      TestDrugs(drug);
    }


  /**
  * Testing doctors and patients
  */

  arrayPerson.add(new Doctor("Tom Baker"));
  arrayPerson.add(new Doctor("Peter Davison"));
  arrayPerson.add(new Doctor("Matt Smith"));
  arrayPerson.add(new Generalpractitian("Paul McGann",345));
  arrayPerson.add(new Generalpractitian("David Tennant",347));
  arrayPerson.add(new Patient(0,"Alexander Pushkin","13039434456","Ibsens gate 5", "0674"));
  arrayPerson.add(new Patient(1,"Per Gynt","14029065467","Waldemar Thranes gate 10", "0632"));
  arrayPerson.add(new Patient(2,"George Orwell","12087856998","Torpsvingen 45", "0345"));

  for (Person p:arrayPerson) {
    testPerson(p);
  }

  /**
  * Testing prescriptions
  */
  Prescription white = new Prescription(0,paracet,"Tom Baker",2, 0, "hvit");
  Prescription blue = new Prescription(1, paracetamol, "Matt Smith", 4, 1, "blaa");

  System.out.println(" ID: " + white.getIdentificationNumber() + " Drug: " +
  white.getDrug().getName() + " Doctor: " + white.getDoctorsName() + " Patient: " +
  white.getPatientsNum() + " Gyldig: " + white.isValid() + " Blue: " +
  white.isBlue());

  System.out.println(" ID: " + blue.getIdentificationNumber() + " Drug: " +
  blue.getDrug().getName() + " Doctor: " + blue.getDoctorsName() + " Patient: " +
  blue.getPatientsNum() + " Gyldig: " + blue.isValid() + " Blue: " +
  blue.isBlue());
}

  public static void testPerson(Person p) {
    if (p instanceof Doctor) {
      Doctor d = (Doctor) p;
      System.out.println("Name: " + d.getName());
    } else if (p instanceof Generalpractitian) {
      Generalpractitian gp = (Generalpractitian) p;
      System.out.println("Name: " + gp.getName() + " Agreement number: " +
      gp.getAgreementNumber());
    } else if (p instanceof Patient) {
      Patient patient = (Patient) p;
      System.out.println(" ID: " + patient.getIdentificationNumber() +
      " Name: " + patient.getName() + " National Identity Number: " +
      patient.getNationalIdentityNumber() + " Address: " +
      patient.getAddress() + " Postal code: " + patient.getIndex());
    }
  }

  public static void TestDrugs(Drug drug) {
    if (drug instanceof OpioidPill) {
        OpioidPill pill = (OpioidPill) drug;
        System.out.println("Name: " + pill.getName() + " ID: " + pill.getIdentificationNumber() +
        " Price: " + pill.getPrice() +  " Opioid: " + pill.getStrength() + " Number of pills: "
        + pill.getNumberOfPills() + " Active Substance: " + pill.getActiveSubstance());
    } else if (drug instanceof OpioidMixture) {
        OpioidMixture mixture = (OpioidMixture) drug;
        System.out.println("Name: " + mixture.getName() + " ID: " + mixture.getIdentificationNumber()
        + " Price: " + mixture.getPrice() + " Opioid: " + mixture.getStrength() +
        " Volume: " + mixture.getVolume() + " Active substance: " + mixture.getActiveSubstance());
    } else if (drug instanceof AddictiveMixture) {
        AddictiveMixture am = (AddictiveMixture) drug;
        System.out.println("Name: " + am.getName() + " ID: " + am.getIdentificationNumber() +
        " Price: " + am.getPrice() + " Addictive: " + am.getAddictive() + " Volume: " +
        am.getVolume() + " Active Substance:" + am.getActiveSubstance());
    } else if (drug instanceof AddictivePill) {
        AddictivePill ap = (AddictivePill) drug;
        System.out.println("Name: " + ap.getName() + " ID: " + ap.getIdentificationNumber() +
        " Price: " + ap.getPrice() + " Addictive: " + ap.getAddictive() +
        " Number of pills: " + ap.getNumberOfPills() + " Active substance: " +
        ap.getActiveSubstance());
    } else if (drug instanceof OrdinaryMixture) {
        OrdinaryMixture om = (OrdinaryMixture) drug;
        System.out.println("Name: " + om.getName() + " ID: " + om.getIdentificationNumber() +
        " Price: " + om.getPrice() + " Volume: " +  om.getVolume() + " Active Substance:" +
        om.getActiveSubstance());
    } else if (drug instanceof OrdinaryPill) {
        OrdinaryPill op = (OrdinaryPill) drug;
        System.out.println("Name: " + op.getName() + " ID: " + op.getIdentificationNumber() +
        " Price: " + op.getPrice() + " Number of pills: " + op.getNumberOfPills() +
        " Active substance: " + op.getActiveSubstance());
    }
  }

}
