import java.util.Arrays;
public class Names {
  //method that sorts all the names and prints them out to the console
  public void sortAndPrint (String[] array) {
    Arrays.sort(array);
    System.out.println("Names have been sorted:");
    for (int i=0; i<array.length;i++) {
      System.out.println(array[i]);
    }
  }
}
