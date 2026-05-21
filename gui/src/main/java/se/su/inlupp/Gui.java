package se.su.inlupp;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
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
    private final BackendControl backendControl = new BackendControl();
    private final SpawnNode spawnNode = new SpawnNode();
    private final MenuBar menuBar = new MenuBar();
    private final ArrayList<GuiCity> guiCities = new ArrayList<>();
    private final ArrayList<Control> buttons = new ArrayList<>();
    private final FileChooser fileChooser = new FileChooser();

    private ObservableList<String> pathList;

    private HBox buttonBox;
    private Pane graphArea;
    private BorderPane borderPane;
    private Stage stage, pathLibraryStage;
    private TextField textfield1 = new TextField();
    private TextField textfield2 = new TextField();
    private Button addCity, screenShotButton, removeCity;

  public void start(Stage stage) {
      stage.setTitle("Train rail finder");
      stage.setScene(buildTrainScene(stage));
      stage.show();
  }

  public Scene buildTrainScene(Stage stage) {
      this.stage = stage;

      buttons.add(menuBar);
      //Paneler
      graphArea = new Pane();
      borderPane = new BorderPane();
      HBox lowerScreenMenu = new HBox();
      HBox upperScreenMenu = new HBox();
      Menu menu = new Menu("Menu");
      menuBar.getMenus().add(menu);

      //MenuButtons
      MenuItem open = new MenuItem("Open");
      open.setOnAction(new OpenHandler());
      MenuItem save = new MenuItem("Save");
      save.setOnAction(new SaveHandler());
      MenuItem exit = new MenuItem("Exit");
      exit.setOnAction((arg) ->{
          saveAndExit();
      });
      menu.getItems().addAll(open, save, exit);

      //Buttons
      addCity = new Button("Add City");buttons.add(addCity);
      removeCity = new Button("Remove City");buttons.add(removeCity);
      screenShotButton = new Button("Save Screenshot");buttons.add(screenShotButton);
      Button pathLibrary = new Button("Path Library");buttons.add(pathLibrary);
      Button linkCities = new Button("Link Cities");buttons.add(linkCities);
      Button findPath = new Button("Find Path");buttons.add(findPath);

      //Placering
      borderPane.setCenter(graphArea);
      lowerScreenMenu.getChildren().addAll(addCity, removeCity, linkCities, findPath, pathLibrary);
      lowerScreenMenu.setAlignment(Pos.CENTER); lowerScreenMenu.setSpacing(10);
      lowerScreenMenu.setStyle("-fx-background-color: green;");
      upperScreenMenu.getChildren().addAll(menuBar, screenShotButton);
      upperScreenMenu.setSpacing(10);
      upperScreenMenu.setStyle("-fx-background-color: green;");

      borderPane.setTop(upperScreenMenu);
      borderPane.setBottom(lowerScreenMenu);

      //Button action handler
      addCity.setOnAction(new AddCityButtonHandler());
      removeCity.setOnAction(new RemoveCityButtonHandler());
      screenShotButton.setOnAction(new SaveScreenShotButtonHandler());
      linkCities.setOnAction(new LinkCitiesButtonHandler());
      findPath.setOnAction(new CreatePathButtonHandler());
      pathLibrary.setOnAction(new PathLibraryButtonHandler());

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

  public class OpenHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        fileChooser.setInitialDirectory(new File("/Users/"));
        File openFile = fileChooser.showOpenDialog(stage);
        System.out.println("Opening file: " + openFile);
    }
  }

  public class SaveHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        fileChooser.setInitialDirectory(new File("/Users/"));
        File openFile = fileChooser.showSaveDialog(stage);
        System.out.println("Saving file: " + openFile);
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

  public class PathLibraryButtonHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e) {

        /*
        try{
            if(pathList.isEmpty()){

                showInformation("No paths have been saved to the library.", "Error");
                return;
            }
        }catch(NullPointerException ex){
            showInformation("No paths have been saved to the library.", "Error");
            return;
        }

        pathList = FXCollections.observableArrayList(mainProgram.getPaths()); Denna är till slutprogrammet
        */

        openMenu();

        pathLibraryStage = new Stage();

        pathList = FXCollections.observableArrayList("Stockholm -> Paris", "Berlin -> Madrid", "Oslo -> Rom");

        buttonBox = new HBox();
        ListView<String> listView = new ListView<>(pathList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button deletePath = new Button("Delete Path");
        Button showPath = new Button("Show Path");
        buttonBox.setDisable(true);
        deletePath.setOnAction(new DeletePathButtonHandler());
        showPath.setOnAction(new ShowPathButtonHandler());
        buttonBox.getChildren().addAll(showPath, deletePath);
        listView.getSelectionModel().selectedItemProperty().addListener(new ButtonActiveHandler());
        FlowPane flowPane = new FlowPane(buttonBox, listView);

        pathLibraryStage.setOnCloseRequest((event) -> {
            closeStage(pathLibraryStage);
        });

        setupWindow(pathLibraryStage, "Path Library", flowPane, 250, 400);
    }
  }

  public class ButtonActiveHandler implements ChangeListener<String> {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
          buttonBox.setDisable(false);
      }
  }

  public class DeletePathButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
          confirmDelete.setTitle("Confirm Delete");
          confirmDelete.setHeaderText("Delete?");
          confirmDelete.setContentText("Are you sure?");
          confirmDelete.showAndWait();
      }
  }

  public class ShowPathButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          closeStage(pathLibraryStage);
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
          flowPane.getChildren().add(1, createTwoTextField("City 1", "City 2"));
          setupWindow(linkCitiesStage, "Link Cities", flowPane, 250, 150);
      }
  }

  public class CreatePathButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          Stage createPathStage = new Stage();
          FlowPane flowPane = createFlowPane(createPathStage, new Confirming("Create Path", createPathStage),"Write the names of the two cities that you\nwould like find a path between.");

          VBox vBox = new VBox(); vBox.setSpacing(10);

          ToggleGroup algorithm = new ToggleGroup();
          RadioButton dijkstra = new RadioButton("Find shortest path by distance (Dijkstra)");
          RadioButton bfs = new RadioButton("Find shortest path by city count (BFS)");
          RadioButton dfs = new RadioButton("Find shortest path by city count (DFS)");
          algorithm.getToggles().addAll(dijkstra, bfs, dfs);
          vBox.getChildren().addAll(createTwoTextField("Start", "Destination"), new Text("Choose search algorithm."), dijkstra, bfs, dfs);
          flowPane.getChildren().add(1, vBox);

          setupWindow(createPathStage, "Create Path", flowPane, 250, 350);
      }
  }

  public class RemoveCityButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          Stage removeCityStage = new Stage();
          FlowPane flowPane = createFlowPane(removeCityStage, new Confirming("RemoveCity", removeCityStage), "Write city to remove");
          TextField removeCity  = new TextField(); removeCity.setPromptText("Enter city name"); removeCity.setPrefWidth(10);
          flowPane.getChildren().add(1, removeCity); textfield1 = removeCity;
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
      for(Control button : buttons){
          button.setDisable(true);
      }
  }

  public void closeStage(Stage stage) {
      if(stage != null){stage.close();}

      for(Control button : buttons){
          button.setDisable(false);
      }
  }

  public void showInformation(String message, String title){
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle(title);
      alert.setHeaderText(message);
      alert.showAndWait();
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

  private HBox createTwoTextField(String text1, String text2){
      HBox textFieldBox = new HBox(); textFieldBox.setAlignment(Pos.CENTER); textFieldBox.setSpacing(5);
      TextField field1 = new TextField(); TextField field2 = new TextField();
      field1.setPromptText(text1); field2.setPromptText(text2);
      field1.setPrefWidth(75); field2.setPrefWidth(75);
      textFieldBox.getChildren().addAll(field1, field2);
      textfield1 = field1;
      textfield2 = field2;

      return textFieldBox;
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

  public BackendControl getBackend(){
      return backendControl;
  }

  public static void main(String[] args) {
      launch(args);
  }
}
