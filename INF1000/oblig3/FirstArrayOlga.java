import java.util.Scanner;

public class FirstArrayOlga {
	public static void main (String [] args){
		Scanner inn = new Scanner(System.in);

		// a
		int[] tall = new int [4];
		tall[0] = 0;
		tall[1] = 1;
		tall[2] = 2;
		tall[3] = 3;

		// b zhdes nuzhen zikl while ili for
		int[] tall1 = {0,1,2,3};

		// c pravilno!
		tall[0] = 1337;
		tall[4] = 1337;

		// d
		String navn1 = inn.nextLine();
		String navn2 = inn.nextLine();
		String navn3 = inn.nextLine();
		String navn4 = inn.nextLine();
		String navn5 = inn.nextLine();
		String[] navn = {navn1,navn2,navn3,navn4,navn5};

		// e
		System.out.println ("Tast inn fem navn:");
		String name;
		int antall = 0;
		while (antall < 5){
		name = inn.nextLine();
		antall+=1;
		}

		System.out.println("Innhold i int-arrayen:");
		int antall1 = 0;
		while (antall1<4){
			System.out.println(tall[0]);
			antall1+=1;
		}
		}
	}
