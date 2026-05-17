package se.su.inlupp;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static javafx.application.Platform.exit;


public class Gui extends Application {

    private Pane graphArea;

    private final SpawnNode spawnNode = new SpawnNode();

    private Button addCity, screenShotButton;



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
          saveAndExit();
      });

      menuBar.setAlignment(Pos.CENTER);
      menuBar.getChildren().addAll(nameLabel, startButton, saveButton, exitButton);

      return new Scene(menuBar, 1000, 500);
  }

  public Scene buildTrainScene(Stage stage) {
      BorderPane borderPane = new BorderPane();
      HBox lowerScreenMenu = new HBox();
      HBox upperScreenMenu = new HBox();
      graphArea = new Pane();

      addCity = new Button("Add City");
      screenShotButton = new Button("Save Screenshot");
      Button mainMenuButton = new Button("Main Menu");
      Button pathLibrary = new Button("Path Library");
      Button removeCity = new Button("Remove City");
      Button linkCities = new Button("Link Cities");
      Button findPath = new Button("Find Path");

      borderPane.setCenter(graphArea);
      lowerScreenMenu.getChildren().addAll(addCity, removeCity, linkCities, findPath, pathLibrary);
      lowerScreenMenu.setAlignment(Pos.CENTER); lowerScreenMenu.setSpacing(10);
      lowerScreenMenu.setStyle("-fx-background-color: green;");
      upperScreenMenu.getChildren().addAll(mainMenuButton, screenShotButton);
      upperScreenMenu.setSpacing(10);
      upperScreenMenu.setStyle("-fx-background-color: green;");

      borderPane.setTop(upperScreenMenu);
      borderPane.setBottom(lowerScreenMenu);

      addCity.setOnAction(new AddCityButtonHandler());
      screenShotButton.setOnAction(new SaveScreenShotButtonHandler());

      mainMenuButton.setOnAction((arg) ->{ //Denna kan bytas ut med en inre klass eftersom att det kommer ske ofta
          System.out.println("Back to Main Menu");
          stage.setScene(buildMenuScene(stage));
          stage.show();
      });


      return new Scene(borderPane, 1000, 500);
  }

  public void saveAndExit() {

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Save and Exit");
      alert.setHeaderText("You are about to exit without saving. Any unsaved data will get lost :(");
      alert.setContentText("Are you sure?");
      Optional<ButtonType> okButton = alert.showAndWait();

      if(okButton.get().equals(ButtonType.OK)){
          exit();
      }
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

  public class SaveScreenShotButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          try{
              WritableImage writableImage = graphArea.snapshot(null, null);
              BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
              ImageIO.write(bufferedImage, "png", new File("capture.png"));

          }catch(IOException e){
              Alert alert = new Alert(Alert.AlertType.ERROR, "IO Error");
              alert.showAndWait();
          }
      }
  }

  public static void main(String[] args) {
      launch(args);
  }
}
