public class LenkeListe <E extends Comparable<E>> {
  //empty linked list
  private Node first = null;

  /**
  *Adding an object to the linked list
  *@param e object
  */
  public void leggTil(E e) {
    //new node
    Node node = new Node(e);
    //if the list is empty - node is the first element
    if (tom()) {
      first = node;
    } else {
      //if the list isn't empty, we get the first element
      //and going through the list, finding the last element
      //and setting the pointer next to our new node
      Node n = first;
      while (n.getNext()!=null) {
        n = n.getNext();
      }
      n.setNext(node);
    }
  }

  /**
  *Removes the object with min value and returns it
  */

  public E fjernMinste() {
    //if the list is empty it returns null
    if (tom())
    return null;
    //suppose the first element has min value
    Node min = first;
    Node n = first;
    Node prev = null;
    //previous object for minimum
    Node mprev = null;
    //going through the linked list...
    while (n.getNext()!=null) {
      prev = n;
      n = n.getNext();
      //comparing the value of min and the object in our list
      if (min.getE().compareTo(n.getE())>0) {
        min = n;
        //Setting previous for minimum
        mprev = prev;
      }
    }
    //Found our minumum!

    //if there is only one element, we remove it
    if (prev==null) {
      first = null;
    } else if (mprev == null) //more than one element, but smallest was first
    {
      first = min.getNext();
    } else {
      //smallest element wasn't the first
      mprev.setNext(min.getNext());
    }
    //returns minimum
    return min.getE();
  }

  /**
  *Returns true of false, depending on if there is an element with same
  *value
  *@param e element we want to compare to
  */
  public boolean inneholder(E e) {
    //if the list is empty, returning false...
    if (tom()) {
      return false;
    }
    //taking the first element
    Node n = first;
    do {
      //starting to compare with the first element
      if (e.compareTo(n.getE())==0) {
        return true;
      }
      n = n.getNext();
    } while (n!=null);
    return false;
  }

  /**
  *Returns true if the list is empty
  */
  public boolean tom() {
    return first == null;
  }

  /**
  *Inner class Node
  */
  private class Node {
    private E e;
    private Node next = null;

    /**
    *Constructor
    *@param element
    */
    Node(E e) {
      this.e = e;
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
    *Getting element
    */
    public E getE() {
      return e;
    }
  }
}
