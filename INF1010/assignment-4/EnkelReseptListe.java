import java.lang.Iterable;
import java.util.Iterator;

public class EnkelReseptListe {
  //empty linked list
  protected Node first = null;
  protected Node tail = null;

  /**
  *Appends a prescription to the end of the list
  *@param prescription Pescription
  */
  public void add(Prescription prescription) {
    Node node = new Node(prescription);
    //First case - list is empty, adding new element
    if (empty()) {
      first = node;
      tail = node;
      //If the list is not empty, we remember the tail.
    } else {
      Node n = tail;
      n.setNext(node);
      tail = node;
      tail.setPrev(n);
    }
  }

  public Prescription getLast() {
    return tail.getPrescription();
  }

  /**
  *Finding prescription based on identification number and returning it
  *@param identificationNumber Prescription's identification number
  */
  public Prescription find(int identificationNumber){
    //if the list is empty, returning false...
    if (empty()) {
      throw new DrugsException("The list is empty.");
    }
    Node n = first;
    do {
      //starting to compare with the first element
      if (identificationNumber==n.getPrescription().getIdentificationNumber()) {
        return n.getPrescription();
      }
      n = n.getNext();
    } while (n!=null);
    throw new DrugsException("Couldn't find.");
  }

  /**
  *Returns true if the list is empty
  */
  public boolean empty() {
    return first == null;
  }

  /**
  *Returns an iterator over the elements in the list.
  */
  public Iterator iterator() {
    return new Iterator() {
      Node n = first;
      public boolean hasNext() {
        return n != null;
      };
      public Prescription next() {
        Node ret = n;
        n = n.getNext();
        return ret.getPrescription();
      };
      public void remove() {
        throw new UnsupportedOperationException();
      };
    };
  }

    /**
    *Inner class Node
    */
    protected class Node {
    private Prescription prescription;
    private Node next = null;
    private Node prev = null;

    /**
    *Constructor
    *@param prescription Prescription
    */
    Node(Prescription prescription) {
      this.prescription = prescription;
    }

    /**
    *Getting the next node
    */
    public Node getNext() {
      return next;
    }

    /**
    *Setting the next node
    *@param node Node
    */
    public void setNext(Node node) {
      this.next = node;
    }

    /**
    *Getting the previous node
    */
    public Node getPrev() {
      return prev;
    }

    /**
    *Setting the previous node
    *@param node Node
    */
    public void setPrev(Node node) {
      this.prev = node;
    }
    /**
    *Getting element
    */
    public Prescription getPrescription() {
      return prescription;
    }
  }
}
