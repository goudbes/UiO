import java.util.Scanner;
public class Billettpris {
    public static void main (String args[]) {
	int alder;
	Scanner input = new Scanner (System.in);
	System.out.print("What is your age? "); //leser inn alder fra terminalen
	alder = input.nextInt();
	//hvis man er yngre enn 12 eller eldre enn 67.. 
	if (alder < 12 || alder > 67) { 
	    System.out.println("You should pay 25 kr for your ticket");
	} else {
	    System.out.println("You should pay 50 kr for your ticket");
	}

    }
}
	
