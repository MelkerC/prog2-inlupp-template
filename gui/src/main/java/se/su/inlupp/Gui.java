package se.su.inlupp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;


public class Gui extends Application {

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

      startButton.setOnAction((arg) ->{
          System.out.println("Starting train pathfinder");
          stage.setScene(buildTrainScene(stage));
          stage.show();
      });

      saveButton.setOnAction((arg) ->{
          System.out.println("Saving...");
      });

      exitButton.setOnAction((arg) ->{
          System.out.println("Exiting the program");
      });

      menuBar.setAlignment(Pos.CENTER);
      menuBar.getChildren().addAll(nameLabel, startButton, saveButton, exitButton);

      return new Scene(menuBar, 300, 300);
  }

  public Scene buildTrainScene(Stage stage) {
      BorderPane borderPane = new BorderPane();

      Button mainMenuButton = new Button("Main Menu");

      mainMenuButton.setOnAction((arg) ->{
          System.out.println("Back to Main Menu");
          stage.setScene(buildMenuScene(stage));
          stage.show();
      });

      borderPane.setTop(mainMenuButton);

      return new Scene(borderPane, 300, 300);
  }

  public static void main(String[] args) {

      launch(args);
  }
}
