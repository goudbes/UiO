import java.util.Iterator;

public class SELTest {
  public static void main(String[] args) {
    SortertEnkelListe<Doctor> doctors = new SortertEnkelListe<Doctor>();

    doctors.add(new Doctor("Tom Baker"));
    doctors.add(new Doctor("Peter Davison"));
    doctors.add(new Doctor("Matt Smith"));
    doctors.add(new Generalpractitian("Paul McGann",345));
    doctors.add(new Generalpractitian("David Tennant",347));

    System.out.println("Printing:");
    Iterator<Doctor> doc = doctors.iterator();
    int i = 0;
    while(doc.hasNext()) {
      System.out.println("[" + i++ +"]" + " " + doc.next());
    }

    System.out.println("");
    System.out.println("Finding Matt Smith..");
    Doctor d = doctors.find("Matt Smith");
    if (d.getName().equalsIgnoreCase("Matt Smith")) {
      System.out.println("Doctor is found.");
    } else {
      System.out.println("Doctor couldn't be found.");
    }

    System.out.println("");
    // System.out.println("Finding Matt Smuth..");
    // d = doctors.find("Matt Smuth");
    // if (d.getName().equalsIgnoreCase("Matt Smuth")) {
    //   System.out.println("Doctor is found.");
    // } else if (d==null) {
    //   System.out.println("Doctor couldn't be found.");
    // }
  }
}
