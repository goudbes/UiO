import java.util.*;
public class SortertEnkelListe<E extends Comparable<E> & Equals>
implements AbstraktSortertEnkelListe <E>{

  //empty linked list
  private Node first = null;

  /**
  *Appends an element to the list in a specified order - min first
  *@param e Element
  */
  public void add(E e) {
    //new node
    Node node = new Node(e);
    //if the list is empty - node is the first element
    if (tom()) {
      first = node;
    } else {
      //If not, comparing the items, starting from the first
      Node n = first;
      Node prev = null;
      while (n!=null) {
        if (node.getE().compareTo(n.getE())>0) {
          break;
        }
        prev = n;
        n = n.getNext();
      }
      node.setNext(n);
      if (prev!=null) {
        prev.setNext(node);
      } else {
        first = node;
      }
    }
  }

  /**
  *Returns an element based on the key
  *@param s Key
  */
  public E find(String s) {
    if (tom()) {
      throw new DrugsException("The list is empty");
    }
    Node n = first;
    while(n!=null) {
      if (n.getE().toString().equalsIgnoreCase(s)) {
        return n.getE();
      }
      n = n.getNext();
    }
    throw new DrugsException("It couldn't be found.");
  }
  /**
  *Returns true if the list contains no elements
  */
  public boolean tom() {
    return first == null;
  }

  /**
  * Returns an iterator over the elements in this list
  */
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      Node n = first;
      public boolean hasNext() {
        return n != null;
      };
      public E next() {
        Node ret = n;
        n = n.getNext();
        return ret.getE();
      };
      public void remove() {
        throw new UnsupportedOperationException();
      };
    };
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
