import java.lang.Iterable;
import java.util.Iterator;

/**
*The user of this interface has a control where the element is inserted.
*The user can add and access the element by their integer position.
*/
public interface AbstraktTabell<E> extends Iterable<E>{
  /**
  *Appends an element to the specified position.
  *@param e element
  *@param i Specified position
  */
  public boolean add(E e, int i);
  /**
  *Returns the element at the specified position
  *@param i Speficied position
  */
  public E find(int i);
  /**
  *Returns an iterator over the elements in this list in proper sequence.
  */
  public Iterator<E> iterator();
}
