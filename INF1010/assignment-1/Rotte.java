public class Rotte extends Animals {
  //Using enum for the state
  public enum RotteTilstand { LEVENDE, SKADET, DOED };
  private String navn;
  private RotteTilstand tilstand;

  //Constructor
  Rotte (String navn) {
    this.navn = navn;
    tilstand = RotteTilstand.LEVENDE;
  }

  /*Printing the state to console

  public void printTilstand() {
  switch(tilstand) {
  case LEVENDE:
  System.out.println("Rotten " + navn +" lever.");
  break;
  case SKADET:
  System.out.println("Rotten " + navn +" er skadet.");
  break;
  case DOED:
  System.out.println("Rotten " + navn +" er doed.");
  break;
  default:
  break;
}
}
*/
public boolean erDoed() {
  return tilstand == RotteTilstand.DOED;
}
//Getting the state
public RotteTilstand hentTilstand() {
  return tilstand;
}

//The rat is attacked by the cat. If it is alive, state is changed to hurt.
//If it is hurt, state is changed to dead.
public void bliAngrepet(Katt katt) {
  System.out.println("Katten " + katt.hentNavn() + " gjorde et angrep paa rotten "
  + navn +".");
  switch(tilstand) {
    case LEVENDE:
    System.out.println("Rotten " + navn + " gikk fra aa vaere levende til aa"
    + " vaere skadet.");
    tilstand = RotteTilstand.SKADET;
    break;
    case SKADET:
    System.out.println("Rotten " + navn + " gikk fra aa vaere skadet til aa"
    + " vaere doed.");
    tilstand = RotteTilstand.DOED;
    break;
    case DOED:
    System.out.println("Doed rotten kan ikke angrepes.");
    break;
    default: break;
  }
}
}
