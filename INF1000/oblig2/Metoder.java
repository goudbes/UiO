// Oppgave 2.1

import java.util.Scanner;

public class Metoder {
    public static void main (String[] args) {
	//calling the method 3 times
	hilsen();
	hilsen();
	hilsen();
    }

    static void hilsen() { //Method
	Scanner input  = new Scanner(System.in);
	System.out.println("Skriv inn navn ");
	String navn = input.nextLine();
	System.out.println("Skriv inn bosted ");
	String bosted = input.nextLine();
	System.out.println("Hei " + navn + "! Du er fra " + bosted+".\n");
	
    }
    
}
