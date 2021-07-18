// Skriv et program som skriver ut 20 tilfeldige numre mellom 0 og 1 og finner det storste tall av dem
public class MinOppgave4{
	public static void main (String[]args){
		int[] tall = new int[20];
		int j = 0;
		for (int i=0; i < tall.length; i++){
			j = (int)(Math.random()*100);
			System.out.println(j);
			tall[i] = j;
		}

		int storste = tall[0];
		int pos = 0;

		for (int i= 0; i < tall.length; i++) {
			if (tall[i]>storste) {
				storste = tall[i];
			}
		}

		System.out.println("The highest value is: " + storste);

	}
}
