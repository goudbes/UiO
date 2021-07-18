//Oblig 3. Oppgave 3.5
//Creare an int array with 20 elements, filling it with random numbers in the range 0-40
//print out the array. Find the element with the highest value and print it out
//together with its position. If there are several elements with the highest value,
//print one of them.

public class MinOppgave3 {
  public static void main (String[] args) {
    int[] anArray =  new int[20]; //declaring an array with 20 elements
    //filling the array with 20 random elements with values between 0-40
    //and printing it out
    for (int i = 0; i < anArray.length; i++) {
      Double x = Math.floor(Math.random() * 40);
      anArray[i] = x.intValue();
      System.out.println("arr[" + i + "]: " + anArray[i]);
    }

    int elementMax = anArray[0];
    int positionMax = 0;
    //finding the element with the highest value and its position
    for (int i = 0; i < anArray.length; i++) {
      if (anArray[i] > elementMax) {
        elementMax = anArray[i];
        positionMax = i;
      }
    }
    //printing the element with the highest value and its position
    System.out.println("\nanArray[" + positionMax +"]: " + elementMax +" has the highest value\n");

  }
}
