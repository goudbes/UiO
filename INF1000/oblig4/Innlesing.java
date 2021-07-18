//Oppgave 4.2 Innlesing
import java.util.Scanner;
import java.io.File;
class Innlesing {
  public static void main(String[] args) throws Exception {
    Scanner in = new Scanner(System.in);
    int i = 0;
    System.out.println("Write a word: "); //getting a word from user
    String userword = in.nextLine();
    Scanner input = new Scanner(new File("winnie.txt"));
    while(input.hasNextLine()) {
      if (input.nextLine().contains(userword)) { //checking how many matches of this word is in the file
        i++;
      }
    }
    System.out.println("Answer:" + i);
  }
}
