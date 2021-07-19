import java.io.Serializable;

public class Row implements Serializable{
  private int id;
  private Grid grid;

  /**
  *Constructs a new row
  *
  *@param grid Grid
  *@param id Row's id
  */
  public Row(Grid grid, int id) {
    this.grid = grid;
    this.id = id;
  }

  /**
  *Returns row's id
  */
  public int getId() {
    return id;
  }
}
