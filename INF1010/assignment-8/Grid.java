import java.io.Serializable;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.Control;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class Grid implements Serializable{
  //Arrays with cells, rows, columns and boxes
  private Cell[][] cells;
  private Column[] columns;
  private Row[] rows;
  private Box[] boxes;

  //Number of solutions
  private static int solution_count = 0;

  private int boxWidth = 0;
  private int boxHeight = 0;
  private int solut_count = 0;


  private int width = 0;


  /**
  *Constructs a grid with specific width
  *
  *@param n Grid width
  *@param boxHeight Box height
  *@param boxWidth Box width
  */
  public Grid(int n, int boxHeight, int boxWidth) {
    width = n;
    if (width>64) {
      throw new IllegalArgumentException ("Grid ["+width +"x" + width +"] is too big");
    }
    this.boxHeight = boxHeight;
    this.boxWidth = boxWidth;
    cells = new Cell[n][n];
    rows = new Row[n];
    columns = new Column[n];

    for (int i = 0; i<n; i++) {
      rows[i] = new Row(this,i);
      columns[i] = new Column(this,i);
    }

    int boxesNum = (n/boxWidth) * (n/boxHeight);

    boxes = new Box[boxesNum];
    for (int i = 0; i<boxes.length;i++) {
      boxes[i] = new Box(this,i);
    }
  }

  /**
  *Clones the grid for saving it in SudokuContainer
  */
  public Grid cloneGrid() throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(this);
    oos.flush();
    oos.close();
    bos.close();
    byte[] byteData = bos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
    Grid grid = (Grid) new ObjectInputStream(bais).readObject();
    return grid;
  }

  /**
  * Returns the width of the box
  * @return box width
  */
  public int getBoxWidth() {
    return boxWidth;
  }

  /**
  * Returns the height of the box
  * @return box height
  */
  public int getBoxHeight() {
    return boxHeight;
  }

  /**
  * Printing Sudoku grid while it is being filled in
  */
  public void debugPrint() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    System.out.println(solution_count);
    this.printToConsole();
    System.out.println("");
    try{
      Thread.sleep(3);
    } catch (InterruptedException e) {}
    }

    /**
    * Finding all possible solutions for the grid and adding them to SudokuContainer
    * @param   row Row
    * @param   column Column
    * @return  returns true if there is solution, false if not
    */
    public boolean fillThisAndTheRest(int row, int column) throws IOException, ClassNotFoundException{
      //debugPrint();
      if (row == width) {
        solution_count++;
        Grid solution = this.cloneGrid();
        SudokuContainer.add(solution);
        solut_count++;
        if(solut_count>3600) {
          return true;
        }
        return false;
      }

      if (cells[row][column].getValue()!=0) {
        if (column==width-1) {
          if (fillThisAndTheRest(row+1,0))
          return true;
        } else {
          if (fillThisAndTheRest(row,column+1))
          return true;
        }

      } else {
        ArrayList<Integer> numbers = cells[row][column].findAllPossibleNumbers(this,cells);
        for (Integer n: numbers) {
          // for (int n = 1; n<=width; n++) {
          //if (cells[row][column].isSafe(n,cells,this)) {
          cells[row][column].setValue(n);
          if (column==width-1) {
            if (fillThisAndTheRest(row+1,0))
            return true;
          } else {
            if (fillThisAndTheRest(row,column+1))
            return true;
          }
          //}
        }
        cells[row][column].setValue(0);
      }
      return false;
    }

    /**
    * Returns array of cells
    */
    public Cell[][] getCell() {
      return cells;
    }

    /**
    * Returns the width of the grid
    * @return width of the grid
    */
    public int getWidth() {
      return width;
    }

    /**
    * Adding a cell to the grid
    * @param  value Value
    * @param  row Row
    * @param  column Column
    */
    public void settCell(int value, int row, int column) {
      cells[row][column] = new Cell(value,rows[row],columns[column],
      whichBox(row,column,boxWidth,boxHeight));
    }

    /**
    * Oversetter et tegn (char) til en tallverdi (int)
    *
    * @param tegn      tegnet som skal oversettes
    * @return          verdien som tegnet tilsvarer
    */
    public static int tegnTilVerdi(char tegn) {
      if (tegn == '.') {                // tom
        return 0;
      } else if ('1' <= tegn && tegn <= '9') {    // tegn er i [1, 9]
        return tegn - '0';
      } else if ('A' <= tegn && tegn <= 'Z') {    // tegn er i [A, Z]
        return tegn - 'A' + 10;
      } else if (tegn == '@') {                   // tegn er @
        return 36;
      } else if (tegn == '#') {                   // tegn er #
        return 37;
      } else if (tegn == '&') {                   // tegn er &
        return 38;
      } else if ('a' <= tegn && tegn <= 'z') {    // tegn er i [a, z]
        return tegn - 'a' + 39;
      } else {                                    // tegn er ugyldig
        throw new IllegalArgumentException("Illegal character");
      }
    }

    /**
    * Oversetter en tallverdi (int) til et tegn (char)
    *
    * @param verdi     verdien som skal oversettes
    * @param tom       tegnet som brukes for aa representere 0 (tom rute)
    * @return          tegnet som verdien tilsvarer
    */
    public static char verdiTilTegn(int verdi) {
      if (verdi == 0) {                           // tom
        return ' ';
      } else if (1 <= verdi && verdi <= 9) {      // tegn er i [1, 9]
        return (char) (verdi + '0');
      } else if (10 <= verdi && verdi <= 35) {    // tegn er i [A, Z]
        return (char) (verdi + 'A' - 10);
      } else if (verdi == 36) {                   // tegn er @
        return '@';
      } else if (verdi == 37) {                   // tegn er #
        return '#';
      } else if (verdi == 38) {                   // tegn er &
        return '&';
      } else if (39 <= verdi && verdi <= 64) {    // tegn er i [a, z]
        return (char) (verdi + 'a' - 39);
      } else {                                    // tegn er ugyldig
        throw new IllegalArgumentException("" + verdi);
      }
    }


    /**
    * Reading the file and creating a new grid
    * @param  filename File name
    * @return          Returns grid
    */
    public static Grid readFile(String filename) throws FileNotFoundException {

      File file = new File(filename);
      int charCounter=0;

      //File not found!
      if(!file.exists()) {
        throw new FileNotFoundException(filename);
      }

      Scanner input = new Scanner(file);

      try {
        int boxHeight = Integer.parseInt(input.nextLine());
        int boxWidth = Integer.parseInt(input.nextLine());
        String line = input.nextLine();
        int n = line.length();
        if (!((n%boxWidth)==0)) {
          throw new IllegalArgumentException("Wrong format: too many characters");
        }
        Grid Grid = new Grid(n, boxHeight, boxWidth);

        int rowCounter = 0;

        for(int r=0;r<n;r++) {
          for (int c=0;c<n;c++) {
            int verdi = tegnTilVerdi(line.charAt(c));
            charCounter++;
            //System.out.println("char: " + charCounter);
            if (verdi<0 || verdi > (boxWidth*boxHeight)) {
              throw new IllegalArgumentException("Value " + verdi +" is outside permitted range");
            }

            if (charCounter>((boxWidth*boxHeight)*(boxWidth*boxHeight))) {
              throw new IllegalArgumentException("Number of characters does not match" + charCounter);
            }
            Grid.settCell(verdi, r, c);
          }
          if(input.hasNextLine()) {
            line = input.nextLine();
            if (!((line.length()%boxWidth)==0)) {
              throw new IllegalArgumentException("Wrong format: too many characters");
            }
          }
        }
        return Grid;

      } catch (NoSuchElementException e) {
        System.out.println("Wrong file format: " + e);
        return null;
      } finally {
        input.close();
      }

    }

    /**
    * Find a box for specific cell
    * @param   row Row
    * @param   column Column
    * @param   boxWidth Width of the box
    * @param   boxHeight Height of the box
    * @return  Returns Box
    */
    public Box whichBox(int row, int column, int boxWidth, int boxHeight) {
      int boxNum = (column/boxWidth)+(row/boxHeight)*(width/boxWidth);
      return boxes[boxNum];
    }

    /**
    *Printing the grid to console
    */
    public void printToConsole() {
      int numberBoxesRow = width/boxWidth;
      //System.out.println(numberBoxesRow);
      //System.out.println(boxWidth);
      for (int i = 0; i < width; i++) {
        if (i%boxHeight==0 && i>0) {
          System.out.println(printDashesX(numberBoxesRow,boxWidth));
        }
        for (int j = 0; j<width;j++) {
          System.out.print(verdiTilTegn(cells[i][j].getValue()));
          if ((j+1)%boxWidth==0) {
            if(j<width-1) {
              System.out.print("|");
            }
          }
        }
        System.out.println();
      }
    }

    /**
    *Printing the grid to gridpane
    */
    public void printToGridPane(GridPane gp) {
      gp.getChildren().clear();
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
          Label label = new Label(Character.toString(verdiTilTegn(cells[i][j].getValue())));
          label.setFont(new Font("Arial Bold", 30));
          gp.add(label, j, i);
        }
      }
      this.layout(gp);
    }

    public void layout(GridPane gp) {
      for (Node n : gp.getChildren()) {
        if (n instanceof Control) {
          Control control = (Control) n;
          control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
          control.setStyle("-fx-background-color: cornsilk  ; -fx-alignment: center;");
        }
      }
      gp.setStyle("-fx-background-color: gray; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2;");
      gp.setSnapToPixel(false);

      ColumnConstraints cc = new ColumnConstraints();
      cc.setPercentWidth(100 / this.getWidth());
      cc.setHalignment(HPos.CENTER);
      RowConstraints rc = new RowConstraints();
      rc.setPercentHeight(100 / this.getWidth());
      rc.setValignment(VPos.CENTER);
      gp.getColumnConstraints().clear();
      gp.getRowConstraints().clear();
      for(int i=0; i<this.getWidth(); i++) {
        gp.getColumnConstraints().add(cc);
        gp.getRowConstraints().add(rc);
      }
    }

    /**
    *Printing the grid to file
    */
    public void printToFile() {
      int numberBoxesRow = width/boxWidth;
      for (int i = 0; i < width; i++) {
        for (int j = 0; j<width;j++) {
          System.out.print(verdiTilTegn(cells[i][j].getValue()));
        }
        System.out.println();
      }
    }

    /**
    * Printing dashes for the grid
    * @param   numberBoxesRow Number of Boxes per row
    * @param   boxWidth width of the box
    * @return  returns line
    */
    public String printDashesX(int numberBoxesRow, int boxWidth) {
      String line = "";
      for (int r=0; r<numberBoxesRow;r++) {
        for (int v = 0; v <boxWidth;v++) {
          line = line + "-";
        }
        if (r<numberBoxesRow-1) {
          line = line + "+";
        }
      }
      return line;
    }
  }
