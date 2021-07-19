public class Test {
  public static void main(String[] args) {
    /**
    *Testing the classes Bil, Elbil, Fossilbil, Lastebil, Personbil
    */

    Bil bil = new Bil("BP12345");
    System.out.println(bil.returnBilNummer());

    Elbil elbil = new Elbil("EL2345",45);
    System.out.println(elbil.returnBilNummer());

    Fossilbil fossilbil = new Fossilbil("BP98765",150.5);
    System.out.println(fossilbil.returnBilNummer() + " " + fossilbil.returnCarbonDioxide());

    Lastebil lastebil = new Lastebil("SR87875",452.5,2550.4);
    System.out.println(lastebil.returnBilNummer() + " " + lastebil.returnCarbonDioxide() +
    " " + lastebil.returnNyttevekt());

    Personbil personbil = new Personbil("AR34345",119.5,8);
    System.out.println(personbil.returnBilNummer() + " " + personbil.returnCarbonDioxide() +
    " " + personbil.returnPassengers());

    Elbil elbil2 = new Elbil("EL4545",35);
    System.out.println(elbil2.returnBilNummer() + " " + elbil2.returnBatteri());

    Personbil personbil2 = new Personbil("DK65437", 135.7,4);
    System.out.println(personbil2.returnBilNummer() + " " + personbil2.returnCarbonDioxide() +
    " " + personbil2.returnPassengers());

    Bil bil2 = new Bil("YF34528");
    System.out.println(bil2.returnBilNummer());
  }
}
