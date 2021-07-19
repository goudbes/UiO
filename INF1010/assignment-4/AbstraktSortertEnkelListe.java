import java.lang.Iterable;
import java.util.Iterator;

/**
*The user of this interface can add elements in a specified sequence -
*minumum first.
*/

public interface AbstraktSortertEnkelListe<E extends Comparable<E> & Equals>
extends Iterable<E> {

  /**
  *Appends an element to the list in specified sequene - minimum first
  *@param e ELement
  */
  public void add(E e);
  /**
  *Returns the element if it exists in the list based on the key
  *@param n Key
  */
  public E find(String n);
  /**
  *Returns an iterator over the elements in this list in proper sequence.
  */
  public Iterator<E> iterator();

}
