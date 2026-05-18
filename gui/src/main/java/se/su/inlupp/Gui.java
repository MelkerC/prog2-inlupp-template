package se.su.inlupp;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.application.Platform.exit;


public class Gui extends Application {

    private Pane graphArea;

    private final SpawnNode spawnNode = new SpawnNode();

    private Button addCity, screenShotButton, removeCity;

    private final ArrayList<GuiCity> guiCities = new ArrayList<>();
    private final ArrayList<Button> buttons = new ArrayList<>();

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

      addCity = new Button("Add City");buttons.add(addCity);
      removeCity = new Button("Remove City");buttons.add(removeCity);
      screenShotButton = new Button("Save Screenshot");buttons.add(screenShotButton);
      Button mainMenuButton = new Button("Main Menu");buttons.add(mainMenuButton);
      Button pathLibrary = new Button("Path Library");buttons.add(pathLibrary);
      Button linkCities = new Button("Link Cities");buttons.add(linkCities);
      Button findPath = new Button("Find Path");buttons.add(findPath);

      borderPane.setCenter(graphArea);
      lowerScreenMenu.getChildren().addAll(addCity, removeCity, linkCities, findPath, pathLibrary);
      lowerScreenMenu.setAlignment(Pos.CENTER); lowerScreenMenu.setSpacing(10);
      lowerScreenMenu.setStyle("-fx-background-color: green;");
      upperScreenMenu.getChildren().addAll(mainMenuButton, screenShotButton);
      upperScreenMenu.setSpacing(10);
      upperScreenMenu.setStyle("-fx-background-color: green;");

      borderPane.setTop(upperScreenMenu);
      borderPane.setBottom(lowerScreenMenu);

      //Button action handler
      addCity.setOnAction(new AddCityButtonHandler());
      removeCity.setOnAction(new RemoveCityButtonHandler());
      screenShotButton.setOnAction(new SaveScreenShotButtonHandler());
      linkCities.setOnAction(new LinkCitiesButtonHandler());

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

          GuiCity city = new GuiCity(x, y, Gui.this);

          guiCities.add(city);
          graphArea.getChildren().add(city);
          graphArea.setOnMouseClicked(null);
          closeStage(null);
      }
  }

  public class AddCityButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          openMenu();
          graphArea.setOnMouseClicked(spawnNode);
      }
  }

  public class LinkCitiesButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent e) {
          Stage linkCitiesStage = new Stage();
          FlowPane flowPane = createFlowPane(linkCitiesStage, new Confirming("LinkCities", linkCitiesStage),"Write the names of the two cities\nthat you would like to link.");

          HBox textFieldBox = new HBox(); textFieldBox.setAlignment(Pos.CENTER); textFieldBox.setSpacing(5);
          TextField city1 = new TextField(); TextField city2 = new TextField();
          city1.setPromptText("City 1"); city2.setPromptText("City 2");
          city1.setPrefWidth(75); city2.setPrefWidth(75);
          textFieldBox.getChildren().addAll(city1, city2);
          flowPane.getChildren().add(1,textFieldBox);

          setupWindow(linkCitiesStage, "Link Cities", flowPane, 250, 150);
      }
  }

  public class RemoveCityButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          Stage removeCityStage = new Stage();
          FlowPane flowPane = createFlowPane(removeCityStage, new Confirming("RemoveCity", removeCityStage), "Write city to remove");
          TextField removeCity  = new TextField(); removeCity.setPromptText("Enter city name"); removeCity.setPrefWidth(10);
          flowPane.getChildren().add(1, removeCity);
          setupWindow(removeCityStage, "Remove City", flowPane,250, 150);
      }
  }

  public void addButton(Button button){
      buttons.add(button);
  }

  public void removeButton(Button button){
      buttons.remove(button);
  }

  public void openMenu(){
      for(Button button : buttons){
          button.setDisable(true);
      }
  }

  public void closeStage(Stage stage) {
      if(stage != null){stage.close();}

      for(Button button : buttons){
          button.setDisable(false);
      }
  }

  public void setupWindow(Stage stage, String stageTitle, Pane pane, double x, double y) {
      stage.setTitle(stageTitle);
      stage.setScene(new Scene(pane, x, y));
      stage.setResizable(false);
      stage.show();
  }

  public FlowPane createFlowPane(Stage stage, EventHandler<ActionEvent> eventHandler, String info) {
      openMenu();
      FlowPane flowPane = new FlowPane();flowPane.setAlignment(Pos.CENTER); flowPane.setOrientation(Orientation.VERTICAL); flowPane.setVgap(10);
      Text infoText = new Text(info);infoText.setTextAlignment(TextAlignment.CENTER);
      Button cancelButton = new Button("Cancel"); Button confirmButton = new Button("Confirm");
      HBox buttonBox = new HBox(); buttonBox.setAlignment(Pos.CENTER); buttonBox.setSpacing(10); buttonBox.getChildren().addAll(confirmButton, cancelButton);

      flowPane.getChildren().addAll(infoText, buttonBox);

      confirmButton.setOnAction(eventHandler);

      cancelButton.setOnAction((event) -> {
          closeStage(stage);
      });

      stage.setOnCloseRequest((event) -> {
          closeStage(stage);
      });
      return flowPane;
  }

  public class Confirming implements EventHandler<ActionEvent> {
      private final String task;
      private final Stage stage;

      public Confirming(String task, Stage stage) {
          this.task = task;
          this.stage = stage;
      }
      @Override
      public void handle(ActionEvent actionEvent) {
          closeStage(stage);

          switch(task){
              case "RemoveCity":
                  System.out.println("Removing city");
                  break;
              case "LinkCities":
                  System.out.println("Linking cities");
                  break;

          }
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
