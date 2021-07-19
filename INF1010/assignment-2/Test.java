public class Test {
  public static void main(String[] args) {
    testBook("1984","George Orwell");
    testLend("1984","George Orwell","Eugene Onegin","Anna Karenina");
    testBorrowing("Lord of the Rings", "J.R.R. Tolkien", "Sven Svensson", "Ivan Petrov");
  }

  public static void testBook (String title, String author) {
    Bok bok = new Bok(title,author);
    System.out.println("Testing if the book returns correct title.");
    if (bok.getName()==null && bok.getAuthor()==null) {
      System.out.println("Test failed.");
      return;
    }
    try {
      if (bok.getName().equalsIgnoreCase(title))
      System.out.println("Book title is " + bok.getName() + " :test passed.");
      else
      System.out.println("Book title: test failed.");
    } catch (Exception e) {
      System.out.println("Test failed due to : " + e);
    }
    System.out.println("Testing if the book returns correct author..");
    try {
      if (bok.getAuthor().equalsIgnoreCase(author))
      System.out.println("Book author is " + author + " : test passed.");
      else
      System.out.println("Book author: test failed.");
    } catch (Exception e) {
      System.out.println("Test failed due to: " + e);
    }
  }

  public static void testLend(String title, String author,String borrower, String borrower_test) {
    Bok bok = new Bok(title, author);
    System.out.println("Testing if the book that is already borrowed can be borrowed again...");
    bok.lend(borrower);
    try{
      if (bok.returnBorrower()==null) {
        System.out.println("Fail.");
        return;
      }

      if (bok.returnBorrower().equalsIgnoreCase(borrower))
      System.out.println("Book is lent and the borrower is " + borrower + ".");
    } catch (Exception e) {
      System.out.println("Test failed due to: " + e);
    }
    try {
      bok.lend(borrower_test);
      System.out.println("Book is already lent to " + bok.returnBorrower() + ": test passed.");
    } catch (Exception e) {
      System.out.println("Test failed due to:" + e);
    }
  }

  public static void testBorrowing(String title,String author,String borrower,String borrower_test) {
    System.out.println("Testing if the book that has been returned can be borrowed again...");
    try {
      Bok bok = new Bok(title,author);
      bok.lend(borrower);
      if (bok.returnBorrower()!=null && bok.returnBorrower().equalsIgnoreCase(borrower)) {
        System.out.println("Book has been borrowed and the borrower is "+ borrower);
      } else {
        System.out.println("Failed to borrow the book");
      }
      bok.returnBack();
      if (bok.returnBorrower()==null){
        System.out.println("Book has been returned back");
      } else {
        System.out.println("Failed to return the book.");
      }

      bok.lend(borrower_test);
      if (bok.returnBorrower()!=null && bok.returnBorrower().equalsIgnoreCase(borrower_test)) {
        System.out.println("Book has been borrowed again by: " + borrower_test +" test passed.");
      } else {
        System.out.println("Book couldn't be borrowed again: test failed.");
      }

    } catch (Exception e) {
      System.out.println("Test failed due to: "+ e);
    }
  }
}
