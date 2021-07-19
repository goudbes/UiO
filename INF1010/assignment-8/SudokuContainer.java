import java.util.ListIterator;

public class SudokuContainer {
  private static Node first = null;
  private static Node tail = null;
  private static int solution_counter = 0;

  /**
  * Adding a solution to the end of linked list. If there are more solutions than
  * 3500, we don't add them but count them anyway
  * @param grid Grid
  */
  public static void add(Grid grid) {
    if (solution_counter<=3501) {
      Node node = new Node(grid);
      solution_counter++;
      if (isEmpty()) {
        first = node;
        tail = node;
      } else {
        Node n = tail;
        n.setNext(node);
        tail = node;
        tail.setPrev(n);
      }
    } else {
      solution_counter++;
    }
  }

  /**
  * Returns number of all solutions
  * @return number of solutions
  */
  public static int getNumberOfSolutions() {
    return solution_counter;
  }

  /**
  * Removes solution from the linked list, FIFO
  * @return Returns solution
  */
  public static Grid remove() {
    Node newTail = tail.getPrev();
    Node n = tail;
    tail = newTail;
    tail.setNext(null);
    solution_counter--;
    return n.getGrid();
  }

  /**
  * Checks if the linked list empty
  * @return true if it is empty, false if it is not
  */
  public static boolean isEmpty() {
    return first == null;
  }

  /**
  * Returns iterator of the linked list
  * @return iterator
  */
  public static ListIterator iterator() {
    return new ListIterator() {
      Node n = first;
      public boolean hasNext() {
        return n != null;
      };
      public boolean hasPrevious() {
        if(n==first) {
          return false;
        }
        if(n==null) {
          n = tail;
        }
        Node current = n.getPrev();
        return current.getPrev() != null;
      };
      public Grid next() {
        Node ret = n;
        n = n.getNext();
        return ret.getGrid();
      };
      public Grid previous() {
        n = n.getPrev();
        Node prev = n.getPrev();
        if(prev==null) {
          return null;
        }
        return prev.getGrid();
      };
      public void remove() {
        throw new UnsupportedOperationException();
      };
      public void add(Object o) {
      };
      public void set(Object o) {
      };
      public int previousIndex() {
        return 0;
      };
      public int nextIndex() {
        return 0;
      };
    };
  }

  private static class Node {
    private Grid grid;
    private Node next = null;
    private Node prev = null;

    /**
    *Constructor
    *@param Grid
    */
    Node(Grid grid) {
      this.grid = grid;
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
    * Returns grid
    * @return grid
    */
    public Grid getGrid() {
      return grid;
    }
  }
}
