import java.util.Scanner;
public class HeiStudent {
    public static void main (String args[]) {
	String navn="Navn";
	int x = 2;
	int y = 20;
	int z,w;
	
	System.out.println("Hei Student!"); //Skriver Hei Student i terminalen
	System.out.println("Hei " + navn); //Skriver ut navnet
	Scanner input = new Scanner(System.in);
	System.out.print("Hva heter du? "); 
	String name = input.nextLine(); //Leser inn navnet fra terminalen
	System.out.println("Hello " + name); //Skriver ut Hello + navnet
	System.out.println("The sum of x and y is "+ (x+y)); //summen av x og y

	//Leser inn to heltall og skriver ut summen
	System.out.print("Enter integer z: ");
	z = input.nextInt();
	System.out.print("Enter integer w: ");
	w = input.nextInt();
	System.out.println("The sum of z and w is: " + (z+w));	
	
    }
}
