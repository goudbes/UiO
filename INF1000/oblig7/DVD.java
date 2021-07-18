/***********************************************
Obligatorisk oppgave 7, inf1000
************************************************/
public class DVD {
  private String title;
  private Person laaner;
  private Person owner;

  /*Constructor*/
  DVD (String title) {
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setOwner(Person person) {
    this.owner = person;
  }

  public Person returnOwner() {
    return this.owner;
  }

  public void setBorrower(Person person) {
    this.laaner = person;
  }

  public Person returnBorrower() {
    return this.laaner;
  }
}
