import java.io.Serializable;

public class Column implements Serializable {
  private int id;
  private Grid grid;

  /**
  *Constructs a new column
  *
  *@param grid Grid
  *@param id Column's id
  */
  public Column(Grid grid, int id) {
    this.id = id;
    this.grid = grid;
  }

  /**
  *Returns columns's id
  */
  public int getId() {
    return id;
  }
}
