import java.util.Scanner;
public class Produkt {
    public static void main (String args[]){
	Scanner input = new Scanner(System.in);
	System.out.print("Enter variable a: ");
	int a = input.nextInt();
	System.out.print("Enter variable b: ");
	int b = input.nextInt();

	int produkt = a * b;
	System.out.println("Produktet av a og b er: " + produkt);

	int diff = a - b;
	if (diff < 0) {
	    System.out.println("Differansen mellom a og b er negativ");
	} else if (diff == 0) {
	    System.out.println("Tallene er like");
	} else if (diff > 0) {
	    System.out.println("Differansen mellom a og b er positiv");
	}
	
    }
}
