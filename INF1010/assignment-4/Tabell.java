import java.util.*;
public class Tabell<F> implements AbstraktTabell<F> {

  private F[] a;
  private int counter = 0;

  /**
  *Constructor
  *Creating a new array with specified size
  *@param arraySize Size of an array
  */
  @SuppressWarnings("unchecked")
  public Tabell(int arraySize) {
    a = (F[]) new Object[arraySize];
  }

  public int getLength() {
    return a.length;
  }
  /**
  *Appends an element to the array with specified position.
  *@param f element
  *@param i Speficied position
  */
  public boolean add(F f, int i) {
    //Checking if the index is out of bounds
    if (i<0) {
      return false;
    }
    //If we have no place in our array, we create a new one
    if (i >= a.length) {
      a = Arrays.copyOf(a, i+1);
    }

    //If the place is free, we put the element
    if (get(i)==null) {
      a[i] = f;
      counter++;
      return true;
    } else {
      //if it is taken...
      return false;
    }

  }

  /**
  *Returns an element at the specified position
  *@param i Specified position
  */
  public F get(int i) {
    if (i>=a.length || i<0) {
      throw new IndexOutOfBoundsException("Index " + i + " is out of bounds.");
    }
    return a[i];
  }

  /**
  *Returns an element at the specified position
  *@param i Specified position
  */
  public F find(int i) {
    return get(i);
  }

  /**
  * Returns an iterator over the elements in this list in proper sequence.
  */
  public Iterator<F> iterator() {
    return new Iterator<F>() {
      int i = 0;
      public boolean hasNext() { return i < a.length; };
      public F next() {
        return a[i++];
       };
      public void remove() {
        throw new UnsupportedOperationException();
      };
    };
  }
}
