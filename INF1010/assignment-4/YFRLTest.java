import java.util.Iterator;

public class YFRLTest {
  public static void main(String[] args) {
    EnkelReseptListe resept = new YngsteForstReseptListe();
    //Adding some drugs
    AddictiveMixture cosylan = new AddictiveMixture("Cosylan",2,127.30,1,300,1.7);
    AddictivePill valium = new AddictivePill("Valium",3,461,2,28,10);
    //Making prescriptions and adding them to the list
    resept.add(new Prescription(0, cosylan, "Matt Smith", 3, 2, "blaa"));
    resept.add(new Prescription(1, valium, "Tom Baker", 5, 1, "hvit"));

    System.out.println("Finding prescription based on id number 0");
    System.out.println(resept.find(0).getDrug().getName());
    System.out.println("Finding prescription based on id number 1");
    System.out.println(resept.find(1).getDrug().getName());
    // System.out.println("Finding prescription based on id number 3");
    // System.out.println(resept.find(3).getDrug().getName());

    System.out.println("");
    System.out.println("Iterator: LIFO");
    Iterator it = resept.iterator();

    resept.add(new Prescription(0, cosylan, "Matt Smith", 3, 2, "blaa"));
    resept.add(new Prescription(1, valium, "Tom Baker", 5, 1, "hvit"));
    int i = 0;
    while (it.hasNext()) {
      Prescription p = (Prescription) it.next();
      System.out.println(p.getDrug().getName());
    }
  }
}
