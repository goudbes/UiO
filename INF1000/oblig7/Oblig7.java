/***********************************************
Obligatorisk oppgave 7, inf1000
************************************************/
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class Oblig7 {
  //ANSI escape codes for formatting output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  private static DVDAdministrasjon admin = new DVDAdministrasjon();

  public static void main(String[] args) throws Exception{

    readFile("dvdarkiv.txt");


    Scanner input = new Scanner(System.in);

    //The menu that appears every time we are done working with the chosen item.
    while (true) {
      System.out.println(" ");
      System.out.println(ANSI_PURPLE + "MENY FOR DVD-ADMINISTRASJON" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "1: Ny person" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "2: Kjoep" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "3: Laan" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "4: Vis" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "5: Oversikt" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "6: Retur" + ANSI_RESET);
      System.out.println(ANSI_YELLOW + "7: Avslutt" + ANSI_RESET);
      System.out.println(" ");
      System.out.print(">");
      while(!input.hasNextInt())
      {
        System.out.println(ANSI_RED + "Invalid input." + ANSI_RESET);
        input.next();
      }
      int number = input.nextInt();
      //calling a method depending on the input.
      switch (number) {
        case 1: createPerson(); break;
        case 2: buyDVD(); break;
        case 3: borrow(); break;
        case 4: overviewPerson(); break;
        case 5: viewAll(); break;
        case 6: giveBackDVD(); break;
        case 7: programExit(); break;
        default:System.out.println(ANSI_RED +"Invalid input." +ANSI_RESET);
        break;
      }
    }
  }


  //Reading the file
  //First, I am adding all the people until it finds a dash,
  //then I have a separate loop where I add a person first, then
  //his DVDs depending if the title starts with a star or not.

  public static void readFile(String filnavn) throws Exception {
    Scanner s = new Scanner(new File(filnavn));
    while (s.hasNext()) {
      String text = s.nextLine();
      if (!text.equals("-")){
        admin.newPerson(text);
      } else {
        break;
      }
    }
    while (s.hasNext()) {
      String text = s.nextLine();
      Person newPerson = admin.findPerson(text);
      if (newPerson!=null) {
        while (s.hasNextLine()) {
          text = s.nextLine();
          if (text.startsWith("*")) {
            DVD dvd = newPerson.buy(text.substring(1));
            newPerson.addToLend(dvd);
            text = s.nextLine();
            Person laaner = admin.findPerson(text);
            dvd.setBorrower(laaner);
            laaner.addToBorrow(dvd);
          } else if(text.equals("-")){
            break;
          }
          else {
            DVD dvd = newPerson.buy(text);
          }
        }
      }
    }
    s.close();
  }

  //Input plus method that creates a new person
  public static void createPerson() {
    Scanner input = new Scanner(System.in);
    System.out.println("Hva heter den nye personen?");
    System.out.print(">");
    String name = input.nextLine();
    while (name.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" +ANSI_RESET);
      return;
    }
    admin.newPerson(name);
  }

  //Input plus method that buys DVD
  public static void buyDVD() {
    Scanner input = new Scanner(System.in);
    System.out.println("Hvem har kjoept DVD-en?");
    System.out.print(">");
    String name = input.nextLine();
    while (name.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" +ANSI_RESET);
      return;
    }
    Person person = admin.findPerson(name);
    if (person==null) {
      System.out.println("Personen finnes ikke.");
      return;
    }
    System.out.println("Hva er tittelen paa DVD-en?");
    System.out.print(">");
    String title = input.nextLine();
    while (title.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" +ANSI_RESET);
      return;
    }
    admin.buyNewDVD(person,title);
  }

  //Input plus method that lets ut to borrow a DVD
  public static void borrow() {
    Scanner input = new Scanner(System.in);
    System.out.println("Hvem vil laane DVD-en?");
    System.out.print(">");
    String name = input.nextLine();
    while (name.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" + ANSI_RESET);
      return;
    }
    Person laaner = admin.findPerson(name);
    if (laaner==null) {
      System.out.println("Personen finnes ikke.");
      return;
    }

    System.out.println("Hvem skal DVD-en laanes fra?");
    System.out.print(">");
    String owner1 = input.nextLine();
    while (owner1.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" +ANSI_RESET);
      return;
    }
    Person owner = admin.findPerson(owner1);
    if (owner==null) {
      System.out.println(ANSI_RED + "Personen finnes ikke." + ANSI_RESET);
      return;
    }

    if (owner.getName().equalsIgnoreCase(laaner.getName())) {
      System.out.println(ANSI_RED + "Kan ikke laane ut DVD-en til den samme personen." + ANSI_RESET);
      return;
    }

    System.out.println("Hva er tittelen på DVD-en?");
    System.out.print(">");
    String title = input.nextLine();
    while (title.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" +ANSI_RESET);
      return;
    }
    admin.borrowDVD(owner,laaner,title);
  }

  //Overview for everyone in the list
  public static void viewAll() {
    admin.overview();
  }

  //Overview for one person or for everyone
  public static void overviewPerson() {
    Scanner input = new Scanner(System.in);
    System.out.println("Hvilken person vil du se? (* for alle)");
    System.out.print(">");
    String person = input.nextLine();
    while (person.equals("")) {
      System.out.println(ANSI_RED + "Invalid input. Leaving an empty string wasn't a good idea :)" + ANSI_RESET);
      return;
    }
    admin.viewPerson(person);
  }


  //Input plus method that returns DVD
  public static void giveBackDVD() {
    Scanner input = new Scanner(System.in);
    System.out.println("Hvilken person som har laant DVD-en?");
    System.out.print(">");
    String laanerr = input.nextLine();
    while (laanerr.equals("")) {
      System.out.println(ANSI_RED +"Invalid input. Leaving an empty string wasn't a good idea :)"+ANSI_RESET);
      return;
    }

    Person laaner = admin.findPerson(laanerr);

    if (laaner==null) {
      System.out.println("Personen finnes ikke.");
      return;
    }

    System.out.println("Hva er tittelen på DVD-en?");
    System.out.print(">");
    String title = input.nextLine();
    while (title.equals("")) {
      System.out.println(ANSI_RED +"Invalid input. Leaving an empty string wasn't a good idea :)" + ANSI_RESET);
      return;
    }
    admin.returnBorrowed(laaner,title);
  }


  //Exiting a program
  public static void programExit() {
    writeToFile();
    System.exit(0);
  }

  //Frivillig:
  //Writing to the file. First I am adding all the people who exist in my
  //liste. Then add dash and for each person I list all the dvds.
  //If the DVD is not in the list for utlaant DVDs, I add it without star.
  //IF the DVD exists in the list utlaant I add the star in the beginning
  //and laaner's name on the next line.
  //All the data is writting in a new file called filewriter.txt
  public static void writeToFile () {
    try {
      //Loop for only people.
      File file = new File("filewriter.txt");
      if (!file.exists()) {
        file.createNewFile();
      }

      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);

      //adding all the people from liste
      for (int i = 0;i<admin.listPeopleSize();i++) {
        Person person = admin.listPeople(i);
        String content = person.getName();
        bw.write(content);
        bw.newLine();
      }
      int k = 0;
      while (k < admin.listPeopleSize()) {
        String dash = "-";
        bw.write(dash);
        bw.newLine();

        Person person = admin.listPeople(k);
        String content = person.getName();
        bw.write(content);
        bw.newLine();

        int j = 0;
        while (j<person.listDVDsize()) {
          DVD dvd = person.listDVD(j);
          if (person.isLend(dvd.getTitle())==null) {
            bw.write(dvd.getTitle());
            bw.newLine();
            j++;
          } else if (person.isLend(dvd.getTitle())!=null) {
            bw.write("*" + dvd.getTitle());
            bw.newLine();
            bw.write(dvd.returnBorrower().getName());
            bw.newLine();
            j++;
          }
        }
        k++;
      }
      bw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
