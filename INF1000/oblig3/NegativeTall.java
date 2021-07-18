
//oblig 3, oppgave 3.4
public class NegativeTall {
  public static void main (String[] args) {
    //declaring int array
    int[] heltall = {1,4,5,-2,-4,6,10,3,-2};
    int i = 0;
    int negative = 0;
    int positionNegative = 0;

    //Counting how many negative numbers we have, getting their
    //position and giving them a new value - their position
    while (i < heltall.length) {
      if (heltall[i] < 0) {
        negative = negative + 1;
        positionNegative = i;
        heltall[i] = positionNegative;
        i++;
      } else {
        i++;
      }
    }

    //printing the amount of negative numbers
    System.out.println("There are " + negative +" negative numbers");

    //Printing the array
    i = 0;
    for (i = 0; i < heltall.length; i++) {
      System.out.println("heltall[" + i + "]: " + heltall[i]);
    }
  }
}
