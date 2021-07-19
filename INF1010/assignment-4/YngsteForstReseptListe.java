public class YngsteForstReseptListe extends EnkelReseptListe {
  @Override
  public void add(Prescription prescription) {
    Node node = new Node(prescription);
    //First case - list is empty, adding new element
    if (empty()) {
      first = node;
      tail = node;
      //If the list is not empty, we put it to beginning of the list.
    } else {
      Node n = first;
      n.setPrev(node);
      first = node;
      first.setNext(n);
    }
  }

  @Override
  public Prescription getLast() {
    return first.getPrescription();
  }
}
