public class Mus extends Animals{
  private String navn;
  private boolean tilstand;

  //Constructor
  Mus (String navn) {
    this.navn = navn;
    tilstand = true;
  }

  //Printing the state, not reeally needed?

  public void printTilstand() {
    if (tilstand==true)
    System.out.println("Musen " + navn + " lever.");
    else
    System.out.println("Musen" + navn  + " er doed.");
  }


  //Returning the state
  public boolean hentTilstand() {
    return tilstand;
  }

  //checking if mouse is dead
  public boolean erDoed() {
    return tilstand == false;
  }

  //Mouse is being attacked. If the mouse is alive I change her state to dead.
  public void bliAngrepet(Katt katt) {
    System.out.println("Katten "+ katt.hentNavn() + " gjorde et angrep paa musen "
    + navn);
    if (tilstand==true) {
      System.out.println("Musen " + navn + " gikk fra aa vaere levende til" +
      " aa vaere doed.");
      tilstand = false;
    } else {
      System.out.println("Doed mus kan ikke angripes.");
    }
  }
}
