//We calculate the volume of a pyramide.
//We ask user to enter the length, width and height of a pyramide and
//then we call the method volum which does the actual caltulations and
//returns the volume.

import java.util.Scanner;
public class MinOppgave2 {
    public static void main (String[] args){
	Scanner input = new Scanner(System.in);
	System.out.println("Hei! Vi skal finne volumet til pyramiden.");
	System.out.println("Lengden til pyramiden: ");
	double l = input.nextDouble();
	System.out.println("Bredden til pyramiden: ");
	double b = input.nextDouble();
	System.out.println("Hoeyden til pyramiden: ");
	double h = input.nextDouble();
	//Calling the method volum
	System.out.println(String.format("\nVolumet til pyramiden er %.02f",volum(l,b,h)));
	
    }

    //Method, which calculates the volume of a pyramide and returns the answer
    static double volum(double l,double b,double h) {
	double v = (l*b*h) / 3;
	return v;
	
    }
}
    
