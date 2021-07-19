public class Lastebil extends Fossilbil{
  private double nyttevekt;

  /**
  *Constructor
  *@param bilnummer Truck registration number
	*@param carbonDioxide The amount of CO2 the truck emits
	*@param nyttevekt Truck's lifting capacity
  */
  Lastebil(String bilnummer, double carbonDioxide, double nyttevekt) {
    super(bilnummer,carbonDioxide);
    this.nyttevekt = nyttevekt;
  }

  /**
  * Getting truck's lifting capacity
  */
  public double returnNyttevekt() {
    return nyttevekt;
  }
}
