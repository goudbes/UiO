public abstract class Person {
  private String name;

  /**
  *Constructor
  *@param name  The name of the person
  */
  Person(String name) {
    this.name = name;
  }
  /**
  *Sets the name of the person
  *@param name The name of the person
  */
  public void setName(String name) {
    this.name = name;
  }
  /**
  *Returns the name of the person
  */
  public String getName() {
    return name;
  }
}
