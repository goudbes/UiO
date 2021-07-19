public class Bol <T extends Animals> {
  private T beboer;

  //checking if it is empty..
  public boolean erTom() {
    return beboer == null;
  }

  //putting in a new resident if house is empty.
  //If the mouse of the rat is dead, then the new resident can be put.
  //I had to check types of beboer, to be able to get it's state.
  public void settInn(T beboer) {
    if (this.erTom()==true)
    this.beboer = beboer;
    else {
      if(this.beboer.erDoed()) {
        this.beboer = beboer;
        return;
      }
      System.out.println("Bolet er allerede fullt.");
    }
  }

  //getting existing resident
  public T henteUt() {
    return this.beboer;
  }
}
