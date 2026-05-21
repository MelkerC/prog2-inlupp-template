package se.su.inlupp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ButtonEvent {

    /*
    public void addButton(Button button){
        buttons.add(button);
    }

    public void removeButton(Button button){
        buttons.remove(button);
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
            FlowPane flowPane = createFlowPane(linkCitiesStage, new Gui.Confirming("LinkCities", linkCitiesStage),"Write the names of the two cities\nthat you would like to link.");
            flowPane.getChildren().add(1, createTwoTextField("City 1", "City 2"));
            setupWindow(linkCitiesStage, "Link Cities", flowPane, 250, 150);
        }
    }

    public class CreatePathButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Stage createPathStage = new Stage();
            FlowPane flowPane = createFlowPane(createPathStage, new Gui.Confirming("Create Path", createPathStage),"Write the names of the two cities that you\nwould like find a path between.");

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
            FlowPane flowPane = createFlowPane(removeCityStage, new Gui.Confirming("RemoveCity", removeCityStage), "Write city to remove");
            TextField removeCity  = new TextField(); removeCity.setPromptText("Enter city name"); removeCity.setPrefWidth(10);
            flowPane.getChildren().add(1, removeCity); textfield1 = removeCity;
            setupWindow(removeCityStage, "Remove City", flowPane,250, 150);
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


            openMenu();

            pathLibraryStage = new Stage();

            pathList = FXCollections.observableArrayList("Stockholm -> Paris", "Berlin -> Madrid", "Oslo -> Rom");

            buttonBox = new HBox();
            ListView<String> listView = new ListView<>(pathList);
            listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            Button deletePath = new Button("Delete Path");
            Button showPath = new Button("Show Path");
            buttonBox.setDisable(true);
            deletePath.setOnAction(new Gui.DeletePathButtonHandler());
            showPath.setOnAction(new Gui.ShowPathButtonHandler());
            buttonBox.getChildren().addAll(showPath, deletePath);
            listView.getSelectionModel().selectedItemProperty().addListener(new Gui.ButtonActiveHandler());
            FlowPane flowPane = new FlowPane(buttonBox, listView);

            pathLibraryStage.setOnCloseRequest((event) -> {
                closeStage(pathLibraryStage);
            });

            setupWindow(pathLibraryStage, "Path Library", flowPane, 250, 400);
        }
    }*/
}
