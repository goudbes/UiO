import java.util.*;
import java.io.*;

public class Oblig3Main {
  public static void main(String[] args) throws Exception {
    /**
    * ArrayList for the cars
    */
    ArrayList<Bil> vehicles = new ArrayList<Bil>();

    try {
    //  Scanner in = new Scanner(System.in);
    //  System.out.print("Enter the file name with extension: ");
      File file = new File(args[0]);

      if(!file.exists()) {
        System.out.println("This file doesn't exist.");
      }

      Scanner input = new Scanner(file);

      while(input.hasNextLine()) {
        addVehicle(vehicles,input.nextLine().split(" "));
      }
      input.close();

      //Casting the object of class Bil to Personbil

      for (Bil b:vehicles){
        if(b instanceof Personbil) {
          Personbil pb = (Personbil) b;
          System.out.println("Personbil: " + pb.returnBilNummer() +
          " CO2: " + pb.returnCarbonDioxide() + " Antall passasjerer: " +
          pb.returnPassengers());
        }
      }
    } catch (Exception ex) {
      System.out.println("Program failed due to: " + ex);
    }
  }

  private static void addVehicle(ArrayList<Bil> vehicles, String[] line) {
    switch (line[0].toUpperCase()) {
      case "BIL":
      vehicles.add(new Bil(line[1]));
      break;
      case "EL":
      vehicles.add(new Elbil(line[1],Integer.parseInt(line[2])));
      break;
      case "FOSSIL":
      vehicles.add(new Fossilbil(line[1],Double.parseDouble(line[2])));
      break;
      case "PERSONFOSSILBIL":
      vehicles.add(new Personbil(line[1],Double.parseDouble(line[2]),
      Integer.parseInt(line[3])));
      break;
      case "LASTEBIL":
      vehicles.add(new Lastebil(line[1],Double.parseDouble(line[2]),
      Double.parseDouble(line[3])));
      break;
      default: break;
    }
  }
}
