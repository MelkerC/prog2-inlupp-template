package se.su.inlupp;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;

import static java.lang.Integer.parseInt;
import static javafx.application.Platform.exit;

public class Gui extends Application {
    private final BackendControl backendControl = new BackendControl();
    private final SpawnNode spawnNode = new SpawnNode();
    private final MenuBar menuBar = new MenuBar();
    private final ArrayList<GuiCity> guiCities = new ArrayList<>();
    private final Map<String, GuiRail> guiRails = new HashMap();
    private final ArrayList<Control> buttons = new ArrayList<>();
    private final FileChooser fileChooser = new FileChooser();
    private final ToggleGroup algorithm = new ToggleGroup();

    private ObservableList<String> pathList;

    private HBox buttonBox;
    private Pane graphArea;
    private BorderPane borderPane;
    private Stage stage, pathLibraryStage;
    private TextField textfield1 = new TextField();
    private TextField textfield2 = new TextField();
    private TextField textfield3 = new TextField();
    private TextField textfield4 = new TextField();
    private Button addCity, screenShotButton, removeCity, disconnectButton;
    private ListView<String> listView;
    private RadioButton dijkstra = new RadioButton("Find shortest path by distance (Dijkstra)");
    private RadioButton bfs = new RadioButton("Find shortest path by city count (BFS)");
    private RadioButton dfs = new RadioButton("Find shortest path by city count (DFS)");
    private VBox algorithmsBox = new VBox();

  public void start(Stage stage) {
      stage.setTitle("Train rail finder");
      setUpRadioButtons();
      stage.setScene(buildTrainScene(stage));
      stage.show();
  }

  private void setUpRadioButtons() {
      dijkstra.setUserData(0);bfs.setUserData(1);dfs.setUserData(2);
      algorithm.getToggles().addAll(dijkstra, bfs, dfs);
      algorithmsBox.getChildren().addAll(dijkstra, bfs, dfs);
      algorithmsBox.setSpacing(10);
  }

  public Scene buildTrainScene(Stage stage) {
      this.stage = stage;

      this.stage.setOnCloseRequest(this::saveAndExit);

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
      exit.setOnAction(this::saveAndExit);
      menu.getItems().addAll(open, save, exit);

      //Buttons
      addCity = new Button("Add City");buttons.add(addCity);
      removeCity = new Button("Remove City");buttons.add(removeCity);
      screenShotButton = new Button("Save Screenshot");buttons.add(screenShotButton);
      disconnectButton = new Button("Disconnect");buttons.add(disconnectButton);
      Button pathLibrary = new Button("Path Library");buttons.add(pathLibrary);
      Button linkCities = new Button("Link Cities");buttons.add(linkCities);
      Button findPath = new Button("Find Path");buttons.add(findPath);

      //Placering
      borderPane.setCenter(graphArea);
      lowerScreenMenu.getChildren().addAll(addCity, removeCity, linkCities, disconnectButton, findPath, pathLibrary);
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
      disconnectButton.setOnAction(new DisconnectButtonHandler());
      linkCities.setOnAction(new LinkCitiesButtonHandler());
      findPath.setOnAction(new CreatePathButtonHandler());
      pathLibrary.setOnAction(new PathLibraryButtonHandler());


      return new Scene(borderPane, 1000, 500);
  }

  public void saveAndExit(Event e) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Save and Exit");
      alert.setHeaderText("You are about to exit without saving. Any unsaved data will get lost :(");
      alert.setContentText("Are you sure?");
      Optional<ButtonType> okButton = alert.showAndWait();

      if(okButton.get().equals(ButtonType.OK)){
          exit();
      }

      if(okButton.get().equals(ButtonType.CANCEL)){
          e.consume();
      }
  }

  public class OpenHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        fileChooser.setInitialDirectory(new File("/Users/"));
        File openFile = fileChooser.showOpenDialog(stage);
        try{
            FileInputStream fis = new FileInputStream(openFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //graphArea = (Map)ois.readObject();

        }catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }
  }

  public class SaveHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        fileChooser.setInitialDirectory(new File("/Users/"));
        File saveFile = fileChooser.showSaveDialog(stage);
        try{
            FileOutputStream fos = new FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(graphArea);//här sparas grafen
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }
  }

  public void spawnNodeFromSave(double x, double y, String name, boolean visited) {
      GuiCity city = new GuiCity(x, y, Gui.this);
      guiCities.add(city);
      graphArea.getChildren().add(city);
      city.createCityNode(name);
      closeStage(null);
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

        pathList = FXCollections.observableArrayList(backendControl.getPaths());

        try{
            if(pathList.isEmpty()){

                showInformation("No paths have been saved to the library.", "Error");
                return;
            }
        }catch(NullPointerException ex){
            showInformation("No paths have been saved to the library.", "Error");
            return;
        }

        openMenu();

        pathLibraryStage = new Stage();

        buttonBox = new HBox();
        listView = new ListView<>(pathList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button deletePath = new Button("Delete Path");
        Button showPath = new Button("Show Path");
        Button changeAlgorithm = new Button("Change Algorithm");
        buttonBox.setDisable(true);
        deletePath.setOnAction((event) ->{
            showConfirmation("Are you sure you want to delete the selected path?", "Delete Path");
        });
        showPath.setOnAction((event) ->{
            String pathDescription = backendControl.getPathByName(listView.getSelectionModel().getSelectedItem()).getPathDescription();
            showInformation("The following is the selected paths description.\n" + pathDescription, "Show Path");
        });
        changeAlgorithm.setOnAction((event) ->{
            Stage changeAlgorithmStage = new Stage();
            FlowPane flowPane = createFlowPane(changeAlgorithmStage, new Confirming("ChangeAlgorithm", changeAlgorithmStage), "Choose Algorithm to change to.");
            flowPane.getChildren().add(1, algorithmsBox);

            setupWindow(changeAlgorithmStage, "Change Algorithm", flowPane, 250, 150);
        });
        buttonBox.getChildren().addAll(showPath, deletePath,  changeAlgorithm);
        listView.setPrefHeight(350);
        listView.getSelectionModel().selectedItemProperty().addListener(new ButtonActiveHandler());
        FlowPane flowPane = new FlowPane(buttonBox, listView);

        pathLibraryStage.setOnCloseRequest((event) -> {
            closeStage(pathLibraryStage);
        });

        setupWindow(pathLibraryStage, "Path Library", flowPane, 350, 400);
    }
  }

  public class ButtonActiveHandler implements ChangeListener<String> {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
          buttonBox.setDisable(false);
      }
  }

  public class AddCityButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          openMenu();
          graphArea.setOnMouseClicked(spawnNode);
      }
  }

    public class DisconnectButtonHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            Stage disconnectStage = new Stage();
            FlowPane flowPane = createFlowPane(disconnectStage, new Confirming("Disconnect", disconnectStage),"Write the names of the two cities\nthat you would like to disconnect.");
            flowPane.getChildren().add(1, createTwoTextField("City 1", "City 2"));
            setupWindow(disconnectStage, "Link Cities", flowPane, 250, 150);
        }
    }

  public class LinkCitiesButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent e) {
          Stage linkCitiesStage = new Stage();
          FlowPane flowPane = createFlowPane(linkCitiesStage, new Confirming("LinkCities", linkCitiesStage),"Write the names of the two cities\nthat you would like to connect and then\nset a name and a distance to the  rail.");

          HBox textFieldBox = new HBox(); textFieldBox.setAlignment(Pos.CENTER); textFieldBox.setSpacing(5);
          TextField field3 = new TextField(); TextField field4 = new TextField();
          field3.setPromptText("Rail name"); field4.setPromptText("Distance in km");
          field3.setPrefWidth(75); field4.setPrefWidth(75);
          textFieldBox.getChildren().addAll(field3, field4);
          textfield3 = field3;
          textfield4 = field4;

          UnaryOperator<TextFormatter.Change> filter = change -> {
              String text = change.getControlNewText();
              if(text.matches("\\d*")){
                  return change;
              }
              return null;
          };

          TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(),null,filter);

          textfield4.setTextFormatter(formatter);

          flowPane.getChildren().add(1, textFieldBox);

          flowPane.getChildren().add(1, createTwoTextField("City 1", "City 2"));
          setupWindow(linkCitiesStage, "Link Cities", flowPane, 250, 350);
      }
  }

  public class CreatePathButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          Stage createPathStage = new Stage();
          FlowPane flowPane = createFlowPane(createPathStage, new Confirming("CreatePath", createPathStage),"Write the names of the two cities that you\nwould like find a path between.");
          VBox vBox = new VBox(); vBox.setSpacing(10);

          vBox.getChildren().addAll(createTwoTextField("Start", "Destination"), new Text("Choose search algorithm."), algorithmsBox);
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

  public void showConfirmation(String message, String title){
      Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
      confirm.setTitle(title);
      confirm.setHeaderText("Confirmation");
      confirm.setContentText(message);

      Optional<ButtonType> okButton = confirm.showAndWait();

      if(okButton.get().equals(ButtonType.OK)){
          switch(title){
              case "Delete Path":
                  backendControl.removePath(backendControl.getPathByName(listView.getSelectionModel().getSelectedItem()));
                  pathList.setAll(backendControl.getPaths());
                  break;
          }
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
          switch(task){
              case "RemoveCity":
                  if(textfield1.getText().isEmpty()){
                      showInformation("You need to fill in the textfield.", "Error");
                      return;
                  }

                  if(backendControl.hasNode(textfield1.getText())){
                      removeTrainRails(backendControl.getEdgesFrom(textfield1.getText()));
                  }

                  if(backendControl.removeCity(textfield1.getText())){
                      showInformation("That city does not exist.", "Error");
                      return;
                  }

                  GuiCity temp = new GuiCity(0,0, Gui.this);
                  for(GuiCity guiCity : guiCities){
                      if(guiCity.getCityName() == null)continue;
                      if(guiCity.getCityName().equals(textfield1.getText())){temp = guiCity;}
                  }

                  temp.removeCity();
                  guiCities.remove(temp);
                  System.out.println(backendControl.updateAllPaths());
                  break;

              case "LinkCities":
                  if(textfield1.getText().isEmpty()){
                      showInformation("Textfield one is empty. Write a city to connect.", "Error");
                      return;
                  }
                  if(textfield2.getText().isEmpty()){
                      showInformation("Textfield two is empty. Write a city to connect.", "Error");
                      return;
                  }
                  if(textfield3.getText().isEmpty()){
                      showInformation("Textfield three is empty. Write a name forthe rail.", "Error");
                      return;
                  }
                  if(textfield4.getText().isEmpty()){
                      showInformation("Textfield four is empty. Write a distance for the rail.", "Error");
                      return;
                  }

                  if(textfield1.getText().equals(textfield2.getText())){
                      showInformation("You can only connect two diffrent cities.", "Error");
                      return;
                  }

                  if(!backendControl.connectCities(textfield1.getText(), textfield2.getText(), textfield3.getText(), parseInt(textfield4.getText()))){
                      showInformation("The cities could not be connected. Either the cities do not exist or they are already connected.", "Error");
                      return;
                  }

                  GuiCity temp1 = new GuiCity(0,0, Gui.this);
                  GuiCity temp2 = new GuiCity(0,0, Gui.this);

                  for(GuiCity guiCity : guiCities){
                      if(guiCity.getCityName().equals(textfield1.getText())){temp1 = guiCity;}
                      if(guiCity.getCityName().equals(textfield2.getText())){temp2 = guiCity;}
                  }

                  GuiRail newRail = new GuiRail(temp1, temp2, textfield3.getText(), parseInt(textfield4.getText()));

                  guiRails.put(textfield3.getText(), newRail);
                  graphArea.getChildren().addFirst(newRail.getVBox());
                  graphArea.getChildren().addFirst(newRail.getLine());

                  //Här skapar den en  gui element  för kanten
                  break;

              case "Disconnect":

                  String tempRail = backendControl.getEdgeNameBetween(backendControl.getCity(textfield1.getText()), backendControl.getCity(textfield2.getText()));
                  if(textfield1.getText().isEmpty() || textfield2.getText().isEmpty()){
                      showInformation("You need to fill in the textfields.", "Error");
                      return;
                  }
                  if(!backendControl.disConnectCities(textfield1.getText(), textfield2.getText())){
                      showInformation("Error", "Error");
                  }
                  removeTrainRails(List.of(tempRail));

                  System.out.println(backendControl.updateAllPaths());

                  break;

              case "CreatePath":
                  if(textfield1.getText().isEmpty() || textfield2.getText().isEmpty()){
                      showInformation("You need to fill in the textfields.", "Error");
                      return;
                  }
                  if(algorithm.getSelectedToggle() == null){
                      showInformation("You need to select an algorithm.", "Error");
                      return;
                  }

                  if(!backendControl.createPath(textfield1.getText(), textfield2.getText(), (int)algorithm.getSelectedToggle().getUserData())){
                      showInformation("The path could not be created.", "Error");
                  }
                  String pathString = backendControl.getPathByName(backendControl.getPaths().getLast()).getPathDescription();

                  showInformation("The path has been created and is saved in the path library.\n" + pathString, "New Path");

                  break;
              case "ChangeAlgorithm":
                  System.out.println("Change Algorithm");
                  backendControl.updatePath(backendControl.getPathByName(listView.getSelectionModel().getSelectedItem()), (int)algorithm.getSelectedToggle().getUserData());
                  pathList.setAll(backendControl.getPaths());
                  break;


          }
          closeStage(stage);
      }
  }

  private void removeTrainRails(List<String> railsToRemove){

      for(String guiRail : railsToRemove){
          graphArea.getChildren().remove(guiRails.get(guiRail).getVBox());
          graphArea.getChildren().remove(guiRails.get(guiRail).getLine());
          guiRails.remove(guiRail);
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
