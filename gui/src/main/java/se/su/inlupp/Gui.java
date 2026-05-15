package se.su.inlupp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;


public class Gui extends Application {

  public void start(Stage stage) {
      stage.setTitle("Train rail finder"); // min

      Pane root = new Pane();
      Label nameLabel = new Label("Name:");
      nameLabel.setPrefWidth(50);
      TextField nameField = new TextField();
      nameField.setPrefWidth(100);
      Button pressMe = new Button("PressMe");
      Label resultLabel = new Label();
      resultLabel.setPrefWidth(100);


      pressMe.setOnAction((arg) -> {
          resultLabel.setText("Hello " + nameField.getText());
      });



      root.getChildren().addAll(nameLabel, nameField, pressMe, resultLabel);

      Scene scene = new Scene(root, 300, 300);

      nameLabel.relocate(10, 50);
      nameField.relocate(10, 50);
      pressMe.relocate(180, 50);
      resultLabel.relocate(100, 200);

      stage.setScene(scene);
      stage.show();





/*
    Graph<String> graph = new ListGraph<String>();
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    Label label =
        new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

    Button button = new Button("Press me");
    VBox root = new VBox(3, label, button);
    root.setAlignment(Pos.CENTER);
    Scene scene = new Scene(root, 640, 900);
    stage.setScene(scene);
    stage.show();
    */
  }

  public static void main(String[] args) {

      launch(args);
  }
}
