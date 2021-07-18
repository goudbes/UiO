import java.util.ArrayList;
/***********************************************
Obligatorisk oppgave 7, inf1000
************************************************/
public class Person {

  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_RESET = "\u001B[0m";

  private String theName;
  private ArrayList<DVD> dvds = new ArrayList<>();
  private ArrayList<DVD> utlaanet = new ArrayList<>();
  private ArrayList<DVD> laanet = new ArrayList<>();

  //Constructor
  Person (String name) {
    this.theName = name;
  }

  //Printing the overview over DVDs for one person
  public void printInfo() {
    System.out.println("Person: " + this.getName());
    System.out.println("Eier: " + dvds.size());
    System.out.println("Laant: " + laanet.size());
    System.out.println("Utlaant: " +utlaanet.size());
    System.out.println(" ");
  }

  //Printing the names of DVDs the person owns. If he/she
  //does not own anything, then i print "personen eier ingenting"
  public void printOwnedDVD() {
    if (dvds.size()!=0) {
      for (DVD dvd:dvds) {
        System.out.println("DVD-en personen eier: " + dvd.getTitle());
      }
    } else if(dvds.size()==0) {
      System.out.println("Personen eier ingenting.");
    }
  }

  //Printing the titles of DVDs that are lend and to whom they are lend.
  public void printLend() {
    if (utlaanet.size()!=0) {
      for (DVD dvd:utlaanet) {
        String title = dvd.getTitle();
        System.out.println("DVD-en som er utlaant:" + title);
        System.out.println(title + " er utlaant til " +dvd.returnBorrower().getName());
        System.out.println(" ");
      }
    } else if(utlaanet.size()==0) {
      System.out.println("Personen laaner ikke bort noe.");
    }
  }

  //Printing information about DVDs that are borrowed and from whom they
  //were borrowed.
  public void printBorrowed() {
    if (laanet.size()!=0) {
      for (DVD dvd:laanet) {
        String title = dvd.getTitle();
        System.out.println("DVD-en som er laant: " +title);
        System.out.println(title + " er laant fra " + dvd.returnOwner().getName());
      }
    } else if(laanet.size()==0) {
      System.out.println("Personen laaner ingenting.");
    }
  }

  //Buying a new DVD and adding the owner of this DVD
  public DVD buy(String title) {
    DVD dvd = new DVD(title);
    dvds.add(dvd);
    dvd.setOwner(this);
    return dvd;
  }

  //Borrowing DVD, and adding the borrower
  public void toBorrow(String dvd1, Person owner) {

    DVD dvd = owner.owns(dvd1);
    if (dvd!=null) {
      laanet.add(dvd);
      dvd.setBorrower(this);
      owner.addToLend(dvd);
    } else {
      System.out.println(ANSI_RED + "DVD-en finnes ikke." +ANSI_RESET);
    }
  }

  public void addToBorrow(DVD dvd) {
    this.laanet.add(dvd);
  }

  public void addToLend(DVD dvd) {
    this.utlaanet.add(dvd);
  }

  //Lending a DVD
  public void toLend(String dvd1, Person laaner) {
    DVD dvd = owns(dvd1);
    if (dvd!=null) {
      utlaanet.add(dvd);
      dvd.setBorrower(laaner);
      laaner.laanet.add(dvd);
    } else {
      System.out.println(ANSI_RED + "DVD-en finnes ikke." +ANSI_RESET);
    }
  }

  //Checking if there is such DVD owned by this person
  public DVD owns(String title) {
    for (DVD dvd:dvds) {
      if (dvd.getTitle().equalsIgnoreCase(title)) {
        return dvd;
      }
    }
    return null;
  }

  //Checking if this DVD is borrowed.
  public DVD isBorrowed(String title) {
    for (DVD dvd:laanet) {
      if (dvd.getTitle().equalsIgnoreCase(title)) {
        return dvd;
      }
    }
    return null;
  }
  //Checking if this DVD is lend to someone.
  public DVD isLend(String title) {
    for (DVD dvd:utlaanet) {
      if (dvd.getTitle().equalsIgnoreCase(title)) {
        return dvd;
      }
    }
    return null;
  }

  //Returning DVD. Removing it from ArrayList laanet from the person
  //who borrowed it and from owners arratlist utlaanet.
  public void returnBack(DVD dvd) {
    this.laanet.remove(dvd);
    Person owner = dvd.returnOwner();
    owner.removeLend(dvd);
  }

  //Method that returns the name of a person
  public String getName() {
    return this.theName;
  }

  //Removing a dvd from owners arraylist utlaanet
  public void removeLend(DVD dvd) {
    this.utlaanet.remove(dvd);
  }


  public DVD listDVD(int i) {
    DVD dvd = dvds.get(i);
    return dvd;
  }

  public int listDVDsize() {
    return dvds.size();
  }

}
