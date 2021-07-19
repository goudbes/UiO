import java.util.*;

public class MainProgram {

  /**********************************
  Using constants to color the output.
  **********************************/
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";


  public static void main(String[] args) {
    int places = 100;
    Hylle<Bok> hylle = new Hylle<Bok>(places);

    //Testing if the bookshelf with 100 places was created.
    //Will fail, if size on line 11 is set to different number.
    System.out.println("\nTrying to create a bookshelf with " + places + " places...");
    if (hylle.size()==places) {
      System.out.println(ANSI_GREEN + "Test passed:" + ANSI_RESET +" Created a bookshelf with " + places +
      " places.");
    } else {
      System.out.println(ANSI_RED + "Test failed:" + ANSI_RESET + "the bookshelf wasn't created.");
    }

    Bok bok = new Bok("1984","Orwell");
    int i = 5;

    //Testing the method insert()
    //Will fail, if line 21:   a[place]=null;
    System.out.println("\nTrying to put the book " + bok.getName() + " on the bookshelf.");
    if (insertTestOK(bok,hylle,i)==true) {
      System.out.println(ANSI_GREEN + "Test passed:"+ ANSI_RESET + " book was put on the bookshelf.");
    } else {
      System.out.println(ANSI_RED + "Test failed:" + ANSI_RESET + "book is not on a bookshelf.");
    }

    //Testing the method take(), checking if the place is empty after we have removed the book
    //Will fail, if line 31: return item != null;
    System.out.println("\nTrying to remove the book " + bok.getName() + " from the bookshelf.");
    if (takeBookTestOK(i,hylle)==true) {
      System.out.println(ANSI_GREEN + "Test passed:" + ANSI_RESET + " The book was removed from the bookshelf.");
    } else {
      System.out.println( ANSI_RED + "Test failed:" + ANSI_RESET +" the book couldn't be removed from the bookshelf.");
    }

    //Testing if it is possible to put the book to the place that does not exist.
    //Fails if insert() returns true
    int place_test=102;
    System.out.println("\nTrying to put a book to the place that does not exist..");
    if (!couldInsert(bok,hylle,place_test)) {
      System.out.println(ANSI_GREEN + "Test passed:" + ANSI_RESET +" Failed to put the book to place " + place_test);
    } else {
      System.out.println(ANSI_RED + "Test failed:" + ANSI_RESET +" the book was put to the place" + place_test);
    }

    //Testing if we can put the book to the place that is already taken
    System.out.println("\nPutting the book to the place that is taken..");
    if (putToTakenPlace(bok,hylle,10)==true) {
      System.out.println( ANSI_RED + "Test failed:" + ANSI_RESET +" Could put the book to the place that is taken.");
    } else {
      System.out.println(ANSI_GREEN + "Test passed:" + ANSI_RESET +"Failed to put the book to the place that is taken.");
    }
  }

  public static boolean insertTestOK(Bok bok,Hylle<Bok> hylle,int i) {
    hylle.insert(bok, i);
    Bok bok_test = (Bok) hylle.returnItem(i);
    return bok_test!= null && bok_test.getName().equals(bok.getName())==true;
  }

  public static boolean takeBookTestOK(int i,Hylle hylle) {
    Bok bok = (Bok) hylle.take(i);
    if (bok==null) {
      return false;
    } else {
    System.out.println("Removing " + bok.getName() + " from the bookshelf.");
    return hylle.isEmpty(i)==true;
    }
  }

  public static boolean couldInsert(Bok bok, Hylle<Bok> hylle, int i) {
    return hylle.insert(bok,i);
  }

  //Trying to put the book to the place that is taken
  public static boolean putToTakenPlace(Bok bok, Hylle<Bok> hylle, int place) {
    hylle.insert(bok,place);
    Bok bok_test = new Bok("War and peace","Leo Tolstoy");
    return hylle.insert(bok_test,place)==true;
  }
}
