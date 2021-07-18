//Oblig 5, oppgave 5.3 - 5.4
public class Isbod {
  private String[] array = new String[10];
  private int antallAnsatte = 0;
  private int i = 0;

  //hiring people, cannot hire more than 10
  public void ansett(String navn) {
    if (antallAnsatte < 10) {
      array[antallAnsatte] = navn;
      antallAnsatte+=1;
    }
    else {
      System.out.println("Sorry, we cannot hire more people.");
    }
  }
  //firing the people
  public void giSistemannSparken() {
    System.out.println(array[antallAnsatte-1] + " is going to be fired.");
    array[antallAnsatte-1] = null;
    antallAnsatte = antallAnsatte-1;
  }
  //printing to the console all the names of the workers
  public void printAlleAnsatte() {
    for (i=0; i<antallAnsatte;i++) {
      System.out.println(array[i]);
    }
  }
}
