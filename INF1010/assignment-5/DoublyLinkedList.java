public class DoublyLinkedList <E extends Comparable<E>> {
  //empty linked list
  private Node first = null;
  private Node tail = null;

  /**
  *Adding an object to the linked list
  *@param e object
  */
  public void leggTil(E e) {
    Node node = new Node(e);
    //First case - list is empty, adding new element
    if (tom()) {
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


  /**
  *Removes the object with min value and returns it
  */
  public E fjernMinste() {
    if (tom()) {
      return null;
    }

    Node n = first;
    Node min = first;

    //Finding the minimum
    while (n.getNext()!=null) {
      n = n.getNext();
      if (min.getE().compareTo(n.getE())>0) {
        min = n;
      }
    }
    //If minimum is the first in the list with at least two elements
    if (min.getPrev()==null && min.getNext()!=null) {
      min.getNext().setPrev(null);
      first = min.getNext();
      //if minimum is the first and the only element
    } else if (min.getPrev()==null && min.getNext()==null) {
      first = null;
      tail = null;
      //if minimum is the last element
    } else if (min.getPrev()!=null && min.getNext()==null) {
      tail = min.getPrev();
      tail.setNext(null);
    } else {
      Node tmpPrev = min.getPrev();
      Node tmpNext = min.getNext();
      tmpPrev.setNext(tmpNext);
      tmpNext.setPrev(tmpPrev);
      }
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

  private class Node {
    private E e;
    private Node next = null;
    private Node prev = null;

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
    public E getE() {
      return e;
    }
  }
}
