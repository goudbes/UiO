/***********************************************
Obligatorisk oppgave 6, inf1000. Deloppgave 6.2
************************************************/

import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class Ordliste {
  private ArrayList<Ord> liste = new ArrayList<Ord>();

  /*Reading all the words from the text file and adding them to
  the array list. We are adding only unique words.*/

  public void lesBok(String filnavn) throws Exception {
    Scanner s = new Scanner(new File(filnavn));
    while (s.hasNextLine()){
      leggTilOrd(s.nextLine());
    }
    s.close();
  }

  /* Method that is adding unique words to the array list,
  if the word is already there we are adding 1 to the counter*/

  private void leggTilOrd(String word) {
    Ord ord = finnOrd(word);
    if (ord != null) {
      ord.oekAntall();
    } else {
      this.liste.add(new Ord(word));
    }
  }

  /*Method that is searching for the word,
  if it does not find it, returns null*/

  public Ord finnOrd(String tekst){
    for(Ord i : liste){
      if(tekst.equalsIgnoreCase(i.toString())) {
        return i;
      }
    }
    return null;
  }

  /*Method that returns the number of unique words in the text*/

  public int antallOrd() {
    return liste.size();
  }

  /*Method that returns the number of occurences of the word in the text.
  If the word exists in the array list, we simply call the method hentAntall()*/

  public int antallForekomster(String tekst) {
    int antallForekomster = 0;
    for(Ord i : liste){
      if(tekst.equalsIgnoreCase(i.toString())) {
        return i.hentAntall();
      }
    }
    return 0;
  }

  /*Method that returnes the word with the highest number of occurences.
  We assume that the first word is the most frequent in the text.*/

  public Ord vanligste() {
    Ord max = liste.get(0);
    for(Ord i: liste) {
      if (i.hentAntall() > max.hentAntall()) {
        max = i;
      }
    }
    return max;
  }

  /*Frivillig: returnerer de ordene som forekommer flest ganger i teksten*/

  public Ord[] alleVanligste() {
    ArrayList<Ord> maxListe = new ArrayList<Ord>();
    Ord max = liste.get(0);
    for(Ord i: liste) {
      if (i.hentAntall() == max.hentAntall()) {
        maxListe.add(i);
      } else if (i.hentAntall() > max.hentAntall()) {
        max = i;
        maxListe.clear();
        maxListe.add(i);
      }
    }
    Ord[] newArray = maxListe.toArray(new Ord[maxListe.size()]);
    return newArray;
  }
}
