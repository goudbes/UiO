public class EldsteForstReseptListe extends EnkelReseptListe {
  @Override
  public void add(Prescription prescription) {
    Node node = new Node(prescription);
    //First case - list is empty, adding new element
    if (empty()) {
      first = node;
      tail = node;
      //If the list is not empty, we put it to the end of the list.
    } else {
      Node n = tail;
      n.setNext(node);
      tail = node;
      tail.setPrev(n);
    }
  }
}
