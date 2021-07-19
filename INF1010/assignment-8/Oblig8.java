import java.util.ArrayList;
import java.util.ListIterator;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Oblig8 extends Application{
  public static void main(String[] args)  throws IOException, ClassNotFoundException{
    Application.launch(args);
  }

  private GridPane gridpane = null;
  private Grid grid = null;
  private ListIterator solutions = null;
  private Label solution_count = new Label();

  private void readFile(String filename) {
    try {
      grid = Grid.readFile(filename);
    }
    catch (FileNotFoundException e) {
      System.out.println("Couldn't found the file.");
    }
    grid.printToGridPane(gridpane);
  }

  private void showSolution(boolean next) {
    if(solutions == null) {
      solutions = SudokuContainer.iterator();
      solution_count.setText(Integer.toString(SudokuContainer.getNumberOfSolutions()));
    }
    Grid solution = null;
    if(next && solutions.hasNext()) {
      solution = (Grid) solutions.next();
    } else if (!next && solutions.hasPrevious()) {
      solution = (Grid) solutions.previous();
    }
    if(solution != null) {
      System.out.println("Solution: ");
      System.out.println(solution.getBoxWidth());
      System.out.println(solution.getBoxHeight());
      solution.printToConsole();
      solution.printToGridPane(gridpane);
      System.out.println("");
    }
  }

  private void sudoku(GridPane gridpane, String[] args) throws IOException, ClassNotFoundException {
    long startTime = System.currentTimeMillis();
    grid.fillThisAndTheRest(0,0);
    showSolution(true);
    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("Total time in ms: " + totalTime);
  }

  private void warning(Exception e) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Warning Dialog");
    alert.setHeaderText("Exception");
    alert.setContentText(e.toString());
    alert.showAndWait();
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Sudoku Solver");
    gridpane = new GridPane();

    VBox vbox = new VBox(10);
    StackPane layout = new StackPane();
    layout.setStyle("-fx-background-color: whitesmoke; -fx-padding: 10;");

    Button fc_btn = new Button("Open file...");
    fc_btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event)   {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        fc.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showOpenDialog(primaryStage);
        if (file != null) {
          //gridpane.getChildren().clear();
          gridpane = new GridPane();
          layout.getChildren().addAll(gridpane);
          try {
            readFile(file.getCanonicalPath());
          } catch(IOException e) {
            warning(e);
          }
        }
      }
    });
    HBox buttons = new HBox(10);
    Button start_btn = new Button("Start");
    start_btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          sudoku(gridpane, null);
        } catch(IOException|ClassNotFoundException e) {
          warning(e);
        }
      }
    });

    Button next_btn = new Button("Next");
    next_btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showSolution(true);
      }
    });

    Button prev_btn = new Button("Prev");
    prev_btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showSolution(false);
      }
    });

    buttons.getChildren().addAll(fc_btn, start_btn, next_btn, prev_btn, solution_count);
    vbox.getChildren().addAll(buttons, layout);

    primaryStage.setScene(new Scene(vbox, 600, 400));
    primaryStage.show();
  }


  /**
  * Writes to file
  * @param filename File name
  */
  public static void writeToFile(String filename){
    try{
      File file = new File(filename);

      if(!file.exists()){
        file.createNewFile();
      }

      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      FileOutputStream f = new FileOutputStream(file);
      System.setOut(new PrintStream(f));
      int i = 0;
      ListIterator it = SudokuContainer.iterator();
      while(it.hasNext()) {
        Grid solution =(Grid) it.next();
        System.out.println("Solution: " + ++i);
        System.out.println(solution.getBoxWidth());
        System.out.println(solution.getBoxHeight());
        solution.printToFile();
        System.out.println("");
      }
      //System.out.println(SudokuContainer.getNumberOfSolutions());
      bw.close();
    }
    catch (IOException e){
      e.printStackTrace();
    } finally{
      System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
  }
}
