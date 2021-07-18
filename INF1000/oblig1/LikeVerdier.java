import java.util.Scanner;
public class LikeVerdier {
    public static void main (String args[]) {
	int c = 5;
	int d = 6;
	//sammenlikner c og d
	if (c == d) {
	    System.out.println( c + " og " + d + " er like");
	} else {
	    System.out.println( c + " er ikke lik " + d);
	}
	//leser inn c og d fra terminalen og sammenlikner dem
	Scanner input = new Scanner(System.in);
	System.out.print("Write an integer c: ");
	    c = input.nextInt();
	System.out.print("Write an integer d: ");
	    d = input.nextInt();

	if (c == d) {
	    System.out.println( c + " og " + d + " er like");
	} else {
	    System.out.println( c + " er ikke lik " + d);
	}

    }
}
