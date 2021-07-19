import java.util.*;

public class Hylle <T> implements GenHylle<T> {
  private T[] a;
  private int size;


  //Constructor, passing the size of the bookshelf
  public Hylle(int size) {
    a = (T[]) new Object[size];
    this.size = size;
  }

  //Putting the item to the shelf, passing the item and the place it is supposed
  //to be on
  public boolean insert(T t, int place) {
    if (place >= a.length-1 || place<0) {
      System.out.println("Error, there is no such place.");
      return false;
    }
    if (a[place]==null) {
      a[place]=t;
      return true;
    } else {
      System.out.println("There is no place on this bookshelf.");
      return false;
    }
  }
  //Checking if the shelf is empty
  public boolean isEmpty(int place) {
    T item = a[place];
    return item == null;
  }

  //Removing the item from the shelf
  public T take(int place) {
    T item = a[place];
    if (item != null) {
      a[place]=null;
      return item;
    } else {
      return null;
    }
  }

  //Getting the item by it's position
  public T returnItem(int i) {
    return a[i];
  }

  //Getting the size of the shelf
  public int size() {
    return a.length;
  }

  //Checking if the shelf has this item
  public boolean doesContain(T t) {
    if (Arrays.asList(a).contains(t)) {
      return true;
    } else {
      return false;
    }
  }
}
