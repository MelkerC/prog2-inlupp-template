package se.su.inlupp;

import com.sun.net.httpserver.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.*;

public class GuiCity extends BorderPane {

    private final Gui gui;
    private final StackPane node = new StackPane();

    private final Circle circle = new Circle(0, 0, 15);;

    public Button createCity = new Button("Create City");

    private double startX, startY;

    private Text cityNameTag;

    private final CheckBox visitedCheck = new CheckBox("Visited");

    private TextField cityName;

    private Stage popUpWindow;

    public GuiCity(double x, double y, Gui gui) {
        relocate(x, y);
        this.gui = gui;
        gui.addButton(createCity);

        circle.setFill(Color.GRAY);

        node.getChildren().addAll(circle, createCity);

        setCenter(node);

        createCity.setOnAction(new CreateCity());

        setOnMousePressed( new StartDragHandler());
        setOnMouseDragged(new DragHandler());

        setOnKeyPressed( new KeyHandler());

        node.setOnMouseClicked((event) -> {
            circle.setFill(Color.ORANGE);
            requestFocus();
        });

        focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue){
                circle.setFill(Color.ORANGE);
                requestFocus();
            }else{
                circle.setFill(Color.GREEN);
            }
        });
    }

    public void removeCity(){
        node.getChildren().clear();
    }

    public class StartDragHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            requestFocus();
            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
        }
    }

    public class DragHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            requestFocus();
            double newX = getLayoutX() + mouseEvent.getX() - startX;
            double newY = getLayoutY() + mouseEvent.getY() - startY;

            relocate(newX, newY);
            gui.changeDetected();
        }
    }

    public class KeyHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {
            double x = getLayoutX();
            double y = getLayoutY();

            switch (keyEvent.getCode()) {
                case DOWN:
                    y += 15; break;
                case UP:
                    y -= 15; break;
                case RIGHT:
                    x += 15; break;
                case LEFT:
                    x -= 15; break;
            }
            keyEvent.consume();
            relocate(x, y);
            gui.changeDetected();
        }
    }

    public void createCityNode(String cityName) {
        if(!gui.getBackend().addCity(cityName)){
            gui.showInformation("That city already exists!", "Error");
            return;
        }
        node.getChildren().clear();
        circle.setFill(Color.GREEN);
        cityNameTag = new Text(cityName);
        gui.removeButton(createCity);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(cityNameTag, visitedCheck);

        visitedCheck.setSelected(gui.getBackend().getCity(cityName).getVisited());

        visitedCheck.setOnAction(event -> {
           gui.getBackend().getCity(cityNameTag.getText()).visit();
            gui.changeDetected();
        });

        node.getChildren().addAll(circle, vBox);
        gui.changeDetected();
        closePopup(popUpWindow);
    }

    public void closePopup(Stage popUpWindow) {
        createCity.setDisable(false);
        gui.closeStage(popUpWindow);
    }

    public class Confirming implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(cityName.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error: Missing City name");
                alert.setHeaderText("The city has to have a unique name.");
                alert.showAndWait();
            }else{
                createCityNode(cityName.getText());
            }
        }
    }

    public class CreateCity implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            createCity.setDisable(true);
            popUpWindow = new Stage();

            FlowPane flowPane = gui.createFlowPane(popUpWindow,new Confirming(),"Fill in this form.\nPress \"Create City\" to continue");
            cityName = new TextField(); cityName.setPromptText("City name"); cityName.setPrefWidth(10);
            flowPane.getChildren().add(1, cityName);

            popUpWindow.setOnCloseRequest((event) -> {
                createCity.setDisable(false);
                gui.closeStage(popUpWindow);
            });

            gui.setupWindow(popUpWindow, "Create City", flowPane, 250, 300);
        }
    }

    public String getPos(){
        return getLayoutX() + " " + getLayoutY();
    }

    public StackPane getNode() {
        return node;
    }

    public String getCityName(){
        if(cityNameTag == null){return null;}
        return cityNameTag.getText();
    }
}
