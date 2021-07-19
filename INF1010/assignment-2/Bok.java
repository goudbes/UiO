public class Bok implements TilUtlaan {
  private String title;
  private String author;
  private String borrower;

  //Constructor
  Bok(String title, String author) {
    this.title = title;
    this.author = author;
  }

  //If the book is borrowed, it gets a borrower
  public void lend(String borrower) {
    if (this.borrower==null)
      this.borrower = borrower;
    else
      System.out.println("The book is already lent.");
  }

  //When the book is returned back, we remove the borrower
  public void returnBack() {
    this.borrower = null;
  }

  //Getting the books name
  public String getName() {
    return title;
  }

  //Getting the author
  public String getAuthor() {
    return author;
  }

  //Checking if the book is borrowed
  public String returnBorrower() {
    return borrower;
  }
}
