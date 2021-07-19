
import java.util.*;
public class Oblig2 {
  public static void main(String[] args) {

    Hylle<Bok> hylle = new Hylle<Bok>(100);

    do {
      System.out.println("\n\n          MENU");
      System.out.println("1 - Put a book to a bookshelf ");
      System.out.println("2 - Check if the specific place at the bookshelf is empty");
      System.out.println("3 - Take a book from a bookshelf");
      System.out.println("4 - Exit");
      System.out.print("\nSelect a Menu Option: ");
      try {
        Scanner in = new Scanner(System.in);
        int input = in.nextInt(); // Get user input from the keyboard
        switch (input) {
          case 1:  putBook(hylle);// put a book on a bookshelf
          break;
          case 2:  bookshelfEmpty(hylle);//checking if the specific place is empty
          break;
          case 3:  takeBook(hylle); // Take the book from a bookshelf
          break;
          case 4:  exit(); //exit program
          break;
        }
      } catch (NumberFormatException e) { System.out.println("Error."); }
    }
    while(true); // Display the menu until the user closes the program
  }

  public static void putBook(Hylle<Bok> hylle) {
    Scanner in = new Scanner(System.in);
    System.out.print("Title of the book: ");
    String title = in.nextLine();
    System.out.print("Author of the book: ");
    String author = in.nextLine();
    System.out.print("At which place do you want to put the book?");
    int place = in.nextInt();
    Bok bok = new Bok(title,author);
    hylle.insert(bok,place);
    System.out.println("Book "+ title + " was put on a bookshelf.");
  }

  public static void bookshelfEmpty(Hylle<Bok> hylle) {
    Scanner in = new Scanner(System.in);
    System.out.println("Which place on the bookshelf do you want to check?");
    int place = in.nextInt();
    if (hylle.isEmpty(place)==true) {
      System.out.println("This place on a bookshelf is empty.");
    } else {
      System.out.println("This place on a bookshelf is not empty.");
    }
  }

  public static void takeBook(Hylle<Bok> hylle) {
    Scanner in = new Scanner(System.in);
    System.out.println("From where do you want to take a book?");
    int i = in.nextInt();
    hylle.take(i);
    System.out.println("The book was removed from the bookshelf.");
  }

  public static void exit() {
    System.exit(0);
  }
}
