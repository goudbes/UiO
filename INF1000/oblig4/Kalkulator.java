//Oppgave 4.3 Kalkulator
import java.util.Scanner;

public class Kalkulator{
  public static  void main (String args[]){
    //calling methods with different parameters
    int addSvar = addisjon(3,4);
    int subSvar = subtraksjon(5,2);
    int helDivSvar = heltallsdivisjon(10, 3);
    double divSvar = divisjon(10, 3);

    System.out.println("a) " + addSvar);
    System.out.println("b) " + subSvar);
    System.out.println("c) " + helDivSvar);
    System.out.println("d) " + divSvar);
  }

  public static int addisjon(int a, int b) { //metode addisjon
    return a + b;
  }

  public static int subtraksjon(int a, int b) { //metode subtraksjon
    return a - b;
  }

  public static int heltallsdivisjon (int a, int b) { // metode heltallsdivisjon
    return a / b;
  }

  public static double divisjon (double a, double b) { //metode divisjon
    return a / b;
  }
}
