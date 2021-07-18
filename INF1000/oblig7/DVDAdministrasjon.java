/***********************************************
Obligatorisk oppgave 7, inf1000
************************************************/

import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
public class DVDAdministrasjon {
  //ANSI escape codes for formatting output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_YELLOW = "\u001B[33m";

  private ArrayList<Person> liste = new ArrayList<Person>();

  //Finding person in my arraylist
  public Person findPerson(String personname) {
    for (Person person:liste) {
      if (person.getName().equalsIgnoreCase(personname)) {
        return person;
      }
    }
    return null;
  }

  //Adding a new person
  public void newPerson(String name) {
    this.liste.add(new Person(name));
  }

  //Buying a new DVD
  public void buyNewDVD(Person person,String title){
    if (person.owns(title)==null) {
      person.buy(title);
    } else {
      System.out.println(ANSI_RED + "Denne personen eier denne DVD-en allerede." +ANSI_RESET);
    }
  }


  //Borrowing DVD. Checking if both owner and borrower exist, then
  //checking if the owner owns this DVD and it is not lend to someone else
  //and if the owner is the same person as borrower.
  public void borrowDVD(Person owner, Person laaner,String title){

    if (owner.owns(title)==null) {
      System.out.println(" ");
      System.out.println(ANSI_RED + owner.getName() + " eier ikke denne DVD-en." +ANSI_RESET);
      return;
    }

    if (owner.isLend(title)!=null) {
      System.out.println(" ");
      System.out.println(ANSI_RED +"Denne DVD-en er utlaant til en annen person." + ANSI_RESET);
      return;
    }
    if (owner.getName().equalsIgnoreCase(laaner.getName())) {
      System.out.println(" ");
      System.out.println(ANSI_RED + "Kan ikke lane ut DVD-en til samme person." +ANSI_RESET);
      return;
    }
    laaner.toBorrow(title,owner);
  }

  //Showing information about one person or everyone depending on the input
  public void viewPerson(String name) {
    Person person = findPerson(name);
    if (person!=null && !name.equals("*")) {
      System.out.print("Person: ");
      System.out.println(person.getName());
      System.out.println("DVD-er " + person.getName() + " eier:");
      person.printOwnedDVD();
      person.printLend();
      person.printBorrowed();
      System.out.println(" ");
    } else if (name.equals("*")) {
      if (liste.size()!=0) {
        for (Person p:liste) {
          System.out.print("Person:");
          System.out.println(p.getName());
          p.printOwnedDVD();
          p.printLend();
          p.printBorrowed();
          System.out.println(" ");
        }
      } else if (liste.size()==0) {
        System.out.println(ANSI_RED + "Det finnes ikke noen personer." +ANSI_RESET);
      }
    } else if(person==null) {
      System.out.println(ANSI_RED +"Personen finnes ikke"+ ANSI_RESET);
    }
  }

  //Overview of all the people that exist
  public void overview() {
    if (liste.size()!=0){
      for(Person p:liste) {
        p.printInfo();
      }
    } else if (liste.size()==0) {
      System.out.println(ANSI_RED +"Det finnes ikke noen her." +ANSI_RESET);
    }
  }

  //Returning the borrowed DVD. Checking if this DVD was really borrowed and that
  //borrower exist.
  public  void returnBorrowed(Person laaner, String title) {
    DVD dvd = laaner.isBorrowed(title);

    if (dvd==null) {
      System.out.println(ANSI_RED +"DVD-en ble ikke utlaant til denne personen." + ANSI_RESET);
      return;
    }
    Person owner = dvd.returnOwner();
    laaner.returnBack(dvd);
  }

  public Person listPeople(int i) {
    Person person = liste.get(i);
    return person;
  }


  public int listPeopleSize() {
    return liste.size();
  }


}
