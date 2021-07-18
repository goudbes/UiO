/***********************************************
Obligatorisk oppgave 6, inf1000. Deloppgave 6.1
************************************************/
public class Ord {
  private String theWord;
  private int antallWords = 0;

  //Constructor
  Ord(String tekst) {
    this.theWord = tekst;
    this.oekAntall();
  }
  //Returns the word
  public String toString() {
    return this.theWord;
  }
  //Returns the number of words
  public int hentAntall() {
    return this.antallWords;
  }
  //Count
  public void oekAntall() {
    this.antallWords+=1;
  }

}
