import java.util.Scanner;
public class EnkelKalkulator {
    public static void main (String[] args) {
	Scanner input = new Scanner(System.in);
	System.out.println("Skriv et heltall a:");
	int a = input.nextInt();
	System.out.println("Skriv et heltall b:");
	int b = input.nextInt();
	//calling the methods
	addisjon(a,b);
	substraksjon(a,b);
	multiplikasjon(a,b);
    }
    //metode som printer ut summen med parametre a og b
     public static void addisjon(int a, int b) {
	 System.out.println("Summen av a og b er: " + (a + b));
    }
    //metode som printer ut differansen med parametre a og b
    public static void substraksjon(int a, int b) {
	System.out.println("Differansen av a og b er: " + (a - b));
    }
    //metode som printer ut produktet  med parametre a og b
    public static void multiplikasjon(int a, int b) {
	System.out.println("Produktet av a og b er: " + (a * b) +"\n");
    }
}
