/***********************************************
Obligatorisk oppgave 6, inf1000. Deloppgave 6.3
************************************************/
import java.util.Arrays;
public class Oblig6 {

  //ANSI escape codes for formatting output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_YELLOW = "\u001B[33m";

  public static void main(String[] args) throws Exception {
    Ordliste liste = new Ordliste();
    liste.lesBok("scarlet.text");
    //How many unique words there are in text
    System.out.println("Det er " + ANSI_PURPLE + liste.antallOrd() + ANSI_RESET + " ulike ord i teksten.");
    //How many times the word Holmes appears in text
    System.out.println("Ordet Holmes forekommer " + ANSI_GREEN + liste.antallForekomster("Holmes") + ANSI_RESET + " ganger.");
    //How many times the word elementary appears in text
    System.out.println("Ordet elementary forekommer " + ANSI_YELLOW + liste.antallForekomster("elementary") + ANSI_RESET + " ganger.");
    //most frequent word
    System.out.println("Vanligste ord er " + ANSI_RED + liste.vanligste().toString() + ANSI_RESET);
    /*Frivillig:*/
    //System.out.println(Arrays.toString(liste.alleVanligste()));
  }
}
