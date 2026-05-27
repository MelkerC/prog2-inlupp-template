package se.su.inlupp;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import static java.lang.Double.parseDouble;
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
    private final FileChooser imageChooser = new FileChooser();
    private final ToggleGroup algorithm = new ToggleGroup();

    private ObservableList<String> pathList;

    private HBox buttonBox;
    private Pane graphArea;
    private BorderPane borderPane;
    private Stage stage, pathLibraryStage;
    private TextField textfield1 = new TextField();
    private TextField textfield2 = new TextField();
    private TextField textfield3 = new TextField();
    private Button addCity, screenShotButton, removeCity, disconnectButton;
    private ListView<String> listView;
    private RadioButton dijkstra = new RadioButton("Find shortest path by distance (Dijkstra)");
    private RadioButton bfs = new RadioButton("Find shortest path by city count (BFS)");
    private RadioButton dfs = new RadioButton("Find shortest path by city count (DFS)");
    private final VBox algorithmsBox = new VBox();

    private final SimpleBooleanProperty cantSave = new SimpleBooleanProperty(true);

    private String currantBackgroundName;
    private Image imageBackground;
    private ImageView background;

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

      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

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
      save.disableProperty().bind(cantSave);
      save.setOnAction(new SaveHandler());
      MenuItem exit = new MenuItem("Exit");
      exit.setOnAction(this::saveAndExit);
      menu.getItems().addAll(open, save, exit);

      //Buttons
      addCity = new Button("Add City");buttons.add(addCity);
      removeCity = new Button("Remove City");buttons.add(removeCity);
      screenShotButton = new Button("Save Screenshot");buttons.add(screenShotButton);
      disconnectButton = new Button("Disconnect");buttons.add(disconnectButton);
      Button changeBackground = new Button("Change Background");buttons.add(changeBackground);
      Button pathLibrary = new Button("Path Library");buttons.add(pathLibrary);
      Button linkCities = new Button("Link Cities");buttons.add(linkCities);
      Button findPath = new Button("Find Path");buttons.add(findPath);

      //Placering
      borderPane.setCenter(graphArea);
      lowerScreenMenu.getChildren().addAll(addCity, removeCity, linkCities, disconnectButton, findPath, pathLibrary);
      lowerScreenMenu.setAlignment(Pos.CENTER); lowerScreenMenu.setSpacing(10);
      lowerScreenMenu.setStyle("-fx-background-color: green;");
      upperScreenMenu.getChildren().addAll(menuBar, screenShotButton, changeBackground);
      upperScreenMenu.setSpacing(10);
      upperScreenMenu.setStyle("-fx-background-color: green;");

      borderPane.setTop(upperScreenMenu);
      borderPane.setBottom(lowerScreenMenu);

      //Button action handler
      addCity.setOnAction(new AddCityButtonHandler());
      removeCity.setOnAction(new RemoveCityButtonHandler());
      screenShotButton.setOnAction(new SaveScreenShotButtonHandler());
      disconnectButton.setOnAction(new DisconnectButtonHandler());
      changeBackground.setOnAction(new ChangeBackgroundButtonHandler());
      linkCities.setOnAction(new LinkCitiesButtonHandler());
      findPath.setOnAction(new CreatePathButtonHandler());
      pathLibrary.setOnAction(new PathLibraryButtonHandler());

      background = new ImageView(imageBackground);

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

        Map<String, String> spawnCities = backendControl.loadProgram(openFile);

        graphArea.getChildren().clear();
        guiCities.clear();
        guiRails.clear();

        for(String cityName : spawnCities.keySet()){
            if(cityName.equals("Image")){
                currantBackgroundName = spawnCities.get(cityName);
            }else{
                String[] spawnPos = spawnCities.get(cityName).split("\\s+");
                if(cityName.contains("undeclared")){
                    spawnUndeclaredNode(parseDouble(spawnPos[0]), parseDouble(spawnPos[1]));
                }else{
                    if(!spawnCities.get(cityName).equals("null")){
                        spawnNodeFromSave(cityName, parseDouble(spawnPos[0]), parseDouble(spawnPos[1]));
                    }
                }
            }
        }

        Map<String, String> edges = backendControl.getUniqueEdges();
        for(String from : edges.keySet()){
            Edge<City> edge = backendControl.getEdgesBetween(from, edges.get(from));
            String name = edge.getName();
            String to = edges.get(from);
            int weight = backendControl.getEdgesBetween(from, edges.get(from)).getWeight();

            createRail(getGuiCity(from), getGuiCity(to), name, weight);
        }

        if(!currantBackgroundName.isEmpty()){
            File file = new File("src/main/resources/" + currantBackgroundName);

            if(file.exists()){
                background.setImage(new Image(file.toURI().toString()));

                background.setPreserveRatio(true);
                background.fitWidthProperty().bind(graphArea.widthProperty());
                background.fitHeightProperty().bind(graphArea.heightProperty());

                graphArea.getChildren().remove(background);
                graphArea.getChildren().addFirst(background);
            }
        }
    }
  }

  private GuiCity getGuiCity(String name){
      for(GuiCity guiCity : guiCities){
          if(guiCity.getCityName().equals(name)){
              return guiCity;
          }
      }
      return null;
  }

  public class SaveHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        fileChooser.setInitialDirectory(new File("/Users/"));

        Map<String, String> guiCityPlacements = new HashMap<>();
        Map<String, String> guiEdges = new HashMap<>();

        for(GuiCity guiCity : guiCities){
            guiCityPlacements.put(guiCity.getCityName(), guiCity.getPos());
        }

        for(GuiRail rail : guiRails.values()){
            guiEdges.put(rail.getCity1().getCityName(), rail.getCity2().getCityName());
        }

        File saveFile = fileChooser.showSaveDialog(stage);

        backendControl.saveProgram(saveFile, guiCityPlacements, guiEdges, currantBackgroundName);
        cantSave.set(true);
    }
  }

  public class ChangeBackgroundButtonHandler implements EventHandler<ActionEvent> {
      @Override
      public void handle(ActionEvent actionEvent) {
          imageChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
          imageChooser.setInitialDirectory(new File("/Users/"));
          File openFile = imageChooser.showOpenDialog(stage);

          imageBackground = new Image(openFile.toURI().toString());

          background.setImage(imageBackground);

          currantBackgroundName = "Image_" + System.currentTimeMillis() + ".png";

          File output = new File("src/main/resources/" + currantBackgroundName);

          try{
              BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageBackground, null);
              ImageIO.write(bufferedImage, "png", output);
          }catch(IOException e){
              Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
              alert.setTitle("Error");
              alert.showAndWait();
          }

          background.setPreserveRatio(true);
          background.fitWidthProperty().bind(graphArea.widthProperty());
          background.fitHeightProperty().bind(graphArea.heightProperty());


          graphArea.getChildren().remove(background);
          graphArea.getChildren().addFirst(background);
          changeDetected();
      }
  }

  private void createRail(GuiCity temp1, GuiCity temp2, String tempName, int weight){
      GuiRail newRail = new GuiRail(temp1, temp2, tempName, weight);

      if(temp1.equals(temp2)){
          return;
      }

      if(!guiRails.containsKey(tempName)){
          graphArea.getChildren().addFirst(newRail.getVBox());
          graphArea.getChildren().addFirst(newRail.getLine());
      }
      guiRails.put(tempName, newRail);
  }

  private void spawnNodeFromSave(String name, double x, double y) {
      GuiCity city = new GuiCity(x, y, Gui.this);
      guiCities.add(city);
      graphArea.getChildren().add(city);
      city.createCityNode(name);
      closeStage(null);
  }

  private void spawnUndeclaredNode(double x, double y) {
      GuiCity city = new GuiCity(x, y, Gui.this);
      guiCities.add(city);
      graphArea.getChildren().add(city);
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
          changeDetected();
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
          TextField field4 = new TextField();
          field4.setPromptText("Distance in km");
          field4.setPrefWidth(150);
          textFieldBox.getChildren().add(field4);
          textfield3 = field4;

          UnaryOperator<TextFormatter.Change> filter = change -> {
              String text = change.getControlNewText();
              if(text.matches("\\d*")){
                  return change;
              }
              return null;
          };

          TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(),null,filter);

          textfield3.setTextFormatter(formatter);

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
                  changeDetected();
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

                  if(!backendControl.removeCity(textfield1.getText())){
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
                  backendControl.updateAllPaths();
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

                  String tempName = "Rail between " + textfield1.getText() + " and " + textfield2.getText();

                  if(textfield3.getText().isEmpty()){
                      showInformation("Textfield four is empty. Write a distance for the rail.", "Error");
                      return;
                  }

                  if(textfield1.getText().equals(textfield2.getText())){
                      showInformation("You can only connect two diffrent cities.", "Error");
                      return;
                  }

                  if(!backendControl.connectCities(textfield1.getText(), textfield2.getText(), tempName, parseInt(textfield3.getText()))){
                      showInformation("The cities could not be connected. Either the cities do not exist or they are already connected.", "Error");
                      return;
                  }

                  GuiCity temp1 = new GuiCity(0,0, Gui.this);
                  GuiCity temp2 = new GuiCity(0,0, Gui.this);

                  for(GuiCity guiCity : guiCities){
                      if(guiCity.getCityName() == null)continue;
                      if(guiCity.getCityName().equals(textfield1.getText())){temp1 = guiCity;}
                      if(guiCity.getCityName().equals(textfield2.getText())){temp2 = guiCity;}
                  }

                  int weight = parseInt(textfield3.getText());

                  createRail(temp1, temp2, tempName, weight);

                  backendControl.updateAllPaths();
                  break;

              case "Disconnect":
                  if(textfield1.getText().isEmpty() || textfield2.getText().isEmpty()){
                      showInformation("You need to fill in the textfields.", "Error");
                      return;
                  }
                  if(!backendControl.disConnectCities(textfield1.getText(), textfield2.getText())){
                      showInformation("Cant disconnect those cities", "Error");
                      return;
                  }
                  String tempRail = backendControl.getEdgeNameBetween(backendControl.getCity(textfield1.getText()), backendControl.getCity(textfield2.getText()));
                  removeTrainRails(List.of(tempRail));

                  backendControl.updateAllPaths();

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
                  backendControl.updatePath(backendControl.getPathByName(listView.getSelectionModel().getSelectedItem()), (int)algorithm.getSelectedToggle().getUserData());
                  pathList.setAll(backendControl.getPaths());
                  break;


          }
          changeDetected();
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

  public void changeDetected(){
      cantSave.set(false);
  }

  public BackendControl getBackend(){
      return backendControl;
  }

  public static void main(String[] args) {
      launch(args);
  }
}
