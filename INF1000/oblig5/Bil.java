//Oblig 5 oppgave 5.1
public class Bil {
  private int kmTotal;
  private final double bigtank = 70.0;
  private double tank = bigtank;

//Driving the car if we have enough gasolin
  public void kjorTur(int km) {
    if ((km/10)<=tank) {
      kmTotal += km;
      tank = tank - (km/10.0);
    } else {
      System.out.println("You do not have enough fuel, go get it!");
    }
  }

//Filling the tank with gasolin if there is enough space
  public void fyllTank(double liter) {
    if ((bigtank-tank)>=liter) {
      tank += liter;
    } else {
      System.out.println("There is no space in your fuel tank");
    }
  }

//finding maximum distance the car can drive
  public double hentMaksDistance() {
    return   tank * 10.0;
  }
//Finding the distance the car has been driving
  public int hentKilometerstand() {
    return kmTotal;
  }
}
