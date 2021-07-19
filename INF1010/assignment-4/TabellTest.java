/**
*Testing class Tabell
*/

import java.util.Iterator;

public class TabellTest {
  public static void main(String[] args) {

    Tabell<Drug> tab = new Tabell<Drug>(10);

    OpioidMixture morphine_mix = new OpioidMixture ("Morphine Sulfate Oral Solution",
    0, 300, 3, 5, 20);
    OpioidPill magnus = new OpioidPill("Magnus MR", 1, 250, 3, 10, 30);
    AddictiveMixture cosylan = new AddictiveMixture("Cosylan",2,127.30,1,300,1.7);
    OrdinaryPill paracet = new OrdinaryPill("Paracetamol ratiopharm",5,36,20,500);
    AddictivePill valium = new AddictivePill("Valium",3,461,2,28,10);
    OrdinaryMixture paracetamol = new OrdinaryMixture("Paracetamol",4,99.5,1000,1.22);

    System.out.println(tab.add(morphine_mix,0));
    System.out.println(tab.add(magnus,1));
    System.out.println(tab.add(cosylan,2));

    System.out.println("Trying to put paracet to index 0 that is taken..");
    System.out.println(tab.add(paracet,0));

    System.out.println("");
    System.out.println("Putting paracet to index -1");
    System.out.println(tab.add(paracet,-1));

    System.out.println("");
    System.out.println("Putting valium to index 12");
    System.out.println(tab.add(valium,12));
    System.out.println("Putting paracetamol to index 15");
    System.out.println(tab.add(paracetamol,15));

    System.out.println("");
    System.out.println("Getting a drug from index 15");
    //System.out.println(tab.find(100));
    System.out.println(tab.find(15).getName());

    System.out.println("");
    System.out.println("Iterating through the list..");
    Iterator<Drug> it = tab.iterator();
    int i = 0;
    while(it.hasNext()) {
      System.out.println("[" + i++ +"]" + " " + it.next());
    }
  }
}
