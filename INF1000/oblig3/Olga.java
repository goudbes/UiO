import java.util.Scanner;

public class Olga {
	public static void main(String[] args) {
		Scanner inn = new Scanner(System.in);
		String innlest;
		int tall;
		int sum =0;

		do {
			System.out.println("Skriv et tall: ");
			innlest = inn.nextLine();
			tall = Integer.parseInt(innlest);
			sum = sum + tall;

		}	while (tall!=0);

		System.out.println("Summen av tallene er:" +sum);
	}

}
