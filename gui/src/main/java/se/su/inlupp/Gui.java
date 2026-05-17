package se.su.inlupp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.util.Objects;


public class Gui extends Application {

    private Scene scene;

    private Pane graphArea;

    private final SpawnNode spawnNode = new SpawnNode();

    private Button addCity;



  public void start(Stage stage) {
      stage.setTitle("Train rail finder");

      stage.setScene(buildMenuScene(stage));
      stage.show();
  }

  public Scene buildMenuScene(Stage stage) {
      VBox menuBar = new VBox(25);

      Label nameLabel = new Label("Welcome to the train pathfinder!");
      Button startButton = new Button("Start");
      Button saveButton = new Button("Save");
      Button exitButton = new Button("Exit");

      //StartButton funktionality
      startButton.setOnAction((arg) ->{
          System.out.println("Starting train pathfinder");
          stage.setScene(buildTrainScene(stage));
          stage.show();
      });

      //SaveButton funktionalitet
      saveButton.setOnAction((arg) ->{
          System.out.println("Saving...");
      });

      //ExitButton funktionalitet
      exitButton.setOnAction((arg) ->{
          Stage saveAndExit = new Stage();

          saveAndExit.setTitle("Save before exit?");
          saveAndExit.setScene(saveAndExit());
          saveAndExit.show();
          //exit();
      });

      menuBar.setAlignment(Pos.CENTER);
      menuBar.getChildren().addAll(nameLabel, startButton, saveButton, exitButton);

      return new Scene(menuBar, 1000, 500);
  }

  public Scene buildTrainScene(Stage stage) {
      BorderPane borderPane = new BorderPane();
      HBox lowerScreenMenu = new HBox();
      graphArea = new Pane();

      addCity = new Button("Add City");
      Button mainMenuButton = new Button("Main Menu");
      Button pathLibrary = new Button("Path Library");
      Button removeCity = new Button("Remove City");
      Button linkCities = new Button("Link Cities");
      Button findPath = new Button("Find Path");

      borderPane.setCenter(graphArea);
      lowerScreenMenu.getChildren().addAll(addCity, removeCity, linkCities, findPath, pathLibrary);
      lowerScreenMenu.setAlignment(Pos.CENTER); lowerScreenMenu.setSpacing(10);
      lowerScreenMenu.setStyle("-fx-background-color: green;");

      borderPane.setTop(mainMenuButton);
      borderPane.setBottom(lowerScreenMenu);

      addCity.setOnAction(new AddCityButtonHandler());



      mainMenuButton.setOnAction((arg) ->{ //Denna kan bytas ut med en inre klass eftersom att det kommer ske ofta
          System.out.println("Back to Main Menu");
          stage.setScene(buildMenuScene(stage));
          stage.show();
      });


      return new Scene(borderPane, 1000, 500);
  }

  public Scene saveAndExit() {

      HBox hBox = new HBox(); VBox vBox = new VBox();

      Label textLabel = new Label("Would you like to save before exiting?");
      Button saveButton = new Button("Save and exit");
      Button exitButton = new Button("Dont save");

      hBox.getChildren().addAll(saveButton, exitButton);
      vBox.getChildren().addAll(textLabel, hBox);
      vBox.setAlignment(Pos.CENTER);hBox.setAlignment(Pos.CENTER);
      vBox.setSpacing(10);

      return new Scene(vBox, 300, 200);
  }

  public class SpawnNode implements EventHandler<MouseEvent> {
      @Override
      public void handle(MouseEvent mouseEvent) {
          double x = mouseEvent.getX();
          double y = mouseEvent.getY();

          GuiCity city = new GuiCity(x, y);
          graphArea.getChildren().add(city);
          graphArea.setOnMouseClicked(null);
          addCity.setDisable(false);
      }
  }

  public class AddCityButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          graphArea.setOnMouseClicked(spawnNode);
          addCity.setDisable(true);
      }
  }

  public static void main(String[] args) {

      launch(args);
  }
}
