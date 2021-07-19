public class Katt {
  private String navn;

  //Constructor
  Katt(String navn) {
    this.navn = navn;
  }

  //getting the cat's name
  public String hentNavn() {
    return this.navn;
  }

  /*Hunting. First I get the residents from their houses.
  The cat is trying to catch a mouse if the house is not empty and the mouse is
  alive.If not, he moves on to the rat. If the rat's house is not empty and the
  rat is either hurt or alive, he is trying to catch it.
  */
  public void jakt(Bol<Mus> musebol, Bol<Rotte> rottebol) {
    Mus mouse = musebol.henteUt();
    Rotte rotte = rottebol.henteUt();
    if (!musebol.erTom()==true) {
      if (mouse.hentTilstand()==true) {
        mouse.bliAngrepet(this);
      }
    } else if (!rottebol.erTom()==true) {
      if (rotte.hentTilstand() == Rotte.RotteTilstand.SKADET ||
      rotte.hentTilstand() == Rotte.RotteTilstand.LEVENDE) {
        rotte.bliAngrepet(this);
      }
    } else if (rottebol.erTom()==true || rottebol.henteUt()==null) {
      System.out.println("Katten " + navn + " fant ingen gnagere."
      + " Jakten avsluttes.");
    }
  }
}
