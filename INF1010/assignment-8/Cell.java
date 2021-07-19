import java.util.ArrayList;
import java.io.Serializable;

public class Cell implements Serializable{
  private Row row;
  private Column column;
  private Box box;

  private int value;

  /**
  *Constructs a new cell
  *
  *@param value Cell's value
  *@param row Cell's row
  *@param column Cell's column
  *@param box Cell's box
  */
  public Cell(Integer value, Row row, Column column, Box box) {
    this.row = row;
    this.column = column;
    this.box = box;
    this.value = value;
  }

  /**
  *Returns cell's box
  */
  public Box getBox() {
    return box;
  }

  /**
  *Returns cell's row
  */
  public Row getRow() {
    return row;
  }

  /**
  *Returns cell's column
  */
  public Column getColumn() {
    return column;
  }

  /**
  *Returns cell's value
  */
  public int getValue() {
    return value;
  }

  public boolean setValue(int value) {
    this.value = value;
    return true;
  }


  /**
  *Finds all possible numbers that can be put to the cell within a range
  *
  *@param grid Grid
  *@param cells Array with cells
  */
  public ArrayList<Integer> findAllPossibleNumbers(Grid grid,Cell[][] cells) {
    ArrayList<Integer> possibilities = new ArrayList<Integer>();
    for (int i = 1; i<=grid.getWidth();i++) {
      if ( isBoxSafe(i,cells,grid) && isColumnSafe(i,cells,grid) && isRowSafe(i,cells,grid)) {
      //if ( isRowSafe(i,cells,grid) && isColumnSafe(i,cells,grid) && isBoxSafe(i,cells,grid)) {
      // if (isSafe(i,cells,grid)) {
        possibilities.add(i);
      }
    }
    return possibilities;
  }

  /**
  *Returns true if the cell is empty
  */
  public boolean isEmpty() {
    return value==0;
  }

  public boolean isBoxSafe(int n, Cell[][] cells, Grid grid) {
    if (n>grid.getWidth()) {
      throw new IllegalArgumentException("Value is outside of interval.");
    }

    if (isEmpty()) {
      int boxId = box.getId(); //cell's box

      for (Cell[] rr: cells) {
        for (Cell r:rr) {
          Cell tmp = (Cell) r;
          if (tmp.getBox().getId()==boxId && tmp.getValue()==n) {
            return false;
          }
        }
      }

      return true;
      // int bw = grid.getBoxWidth();
      // int bws = grid.getWidth()/bw;
      // int bh = grid.getBoxHeight();
      // int bhs = grid.getWidth()/bh;
      // int bn = box.getId();
      // int w = grid.getWidth();
      //
      // // System.out.println("START: " + bn);
      // for(int x=0; x<bw; x++)
      //   for(int y=0; y<bh; y++) {
      //     // int id = bh*bw*bn+x + y*w;
      //     // System.out.println(id + ":" + id/w + ":" + id%w + ":" + cells[id/w][id%w].getValue());
      //     // if(cells[id/w][id%w].getValue() == n)
      //     int row = bh * (bn/bhs) + y;
      //     int col = bw * (bn%bws) + x;
      //     // System.out.println(bh + "*" + bn + "/" + bhs + "+" + x);
      //     //  System.out.println(row + ":" + col + ":");
      //     //  System.out.println(cells[row][col].getValue());
      //     if(cells[row][col].getValue() == n)
      //       return false;
      //   }
      //
      // return true;
    } else {
      return false;
    }

  }

  public boolean isRowSafe(int n, Cell[][] cells, Grid grid) {
    if (n>grid.getWidth()) {
      throw new IllegalArgumentException("Value is outside of interval.");
    }
    if (isEmpty()) {
      int x = row.getId();
      //Checking if it's row-safe, return false if not
      for (int j = 0; j<grid.getWidth();j++) {
        if (cells[x][j].getValue()==n) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  public boolean isColumnSafe(int n, Cell[][] cells, Grid grid) {
    if (n>grid.getWidth()) {
      throw new IllegalArgumentException("Value is outside of interval.");
    }
    if (isEmpty()) {
      int y = column.getId(); //cell's column
      for (int i = 0; i<grid.getWidth();i++) {
        if (cells[i][y].getValue()==n) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

    /**
  *Returns true if it is safe to put a number in cell (Checking if there is
  * no such number on the row, column and box)
  *
  *@param n value
  *@param cells Array with cells
  *@param grid Grid
  */
  public boolean isSafe(int n, Cell[][] cells, Grid grid) {
    //Value is outside of interval
    if (n>grid.getWidth()) {
      throw new IllegalArgumentException("Value is outside of interval.");
    }

    //If the cell is empty we start the check..
    if (isEmpty()) {
      int x = row.getId(); //cell's row
      int y = column.getId(); //cell's column
      int boxId = box.getId(); //cell's box
      //Assuming that it's safe
      boolean boxSafe = true;
      boolean columnSafe = true;
      boolean rowSafe = true;

      //Checking if it is box-safe and if it's not we return false
      for (Cell[] rr: cells) {
        for (Cell r:rr) {
          Cell tmp = (Cell) r;
          if (tmp.getBox().getId()==boxId && tmp.getValue()==n) {
            boxSafe = false;
            return false;
          }
        }
      }
      //Checking if it is column-safe and return false if not
      for (int i = 0; i<grid.getWidth();i++) {
        if (cells[i][y].getValue()==n) {
          columnSafe = false;
          return false;
        }
      }

      //Checking if it's row-safe, return false if not
      if (columnSafe==true) {
        for (int j = 0; j<grid.getWidth();j++) {
          if (cells[x][j].getValue()==n) {
            return false;
          }
        }
      }
      return true;

    } else {
      //False, if the value isn't empty
      return false;
    }
  }
}
