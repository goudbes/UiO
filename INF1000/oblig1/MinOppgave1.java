/*My program is checking if the person is a real gamer or not.
It reads from terminal the amount of hours the person is playing per week.
If the person is playing more than 20 hours per week, then he is a real gamer.
If not, the program will tell how many hours more you need to play to become a real gamer.
 */

import java.util.Scanner;
public class MinOppgave1 {
    public static void main (String args[]) {
	int hours, besth;
	      
	//Leser inn antall timer 
	Scanner input = new Scanner(System.in);
	System.out.print("How many hours per week do you play games? ");
	hours = input.nextInt();

	if (hours >= 20) {
	    System.out.println("You are a real gamer :)");
	} else {
	    besth = 20-hours; //Finding additional hours needed
	    System.out.println("You do not play enough, you should play " + besth + " hours more to become a real gamer :-)");
	    
	}

    }
}
	
