import java.io.Serializable;

public class Box implements Serializable {
  private int id;
  private Grid grid;

  /**
  *Constructs a new box
  *@param grid Grid
  *@param id Box's id
  */
  public Box(Grid grid, int id) {
    this.id = id;
    this.grid = grid;
  }

  /**
  *Returns box's id
  */
  public int getId() {
    return id;
  }
}
