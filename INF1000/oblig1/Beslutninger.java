import java.util.Scanner;
public class Beslutninger {
    public static void main (String args[]) {
	int alder = 20; //alder med verdi 20
	//myndig eller ikke
	if (alder>=18) {
	    System.out.println("Du er myndig");
	} else {
	    System.out.println("Du er ikke myndig");
	}
	//alder blir lest inn fra terminalen
	Scanner input = new Scanner(System.in);
	System.out.print("What is your age? ");
	alder = input.nextInt();

       if (alder>=18) {
	    System.out.println("Du er myndig");
	} else {
	    System.out.println("Du er ikke myndig");
	}

    }
}
