package se.su.inlupp;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GuiCity extends BorderPane {

    private Pane node = new Pane();

    private double startX, startY;

    public GuiCity(double x, double y) {
        relocate(x, y);
        Pane root = new Pane();

        Circle circle = new Circle(0, 0, 20);
        circle.setFill(Color.GREEN);

        root.getChildren().add(circle);

        //Create window för att skapa en city ---------------

        setCenter(root);

        setOnMousePressed( new StartDragHandler());
        setOnMouseDragged(new DragHandler());
    }

    public class StartDragHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
        }
    }

    public class DragHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double newX = getLayoutX() + mouseEvent.getX() - startX;
            double newY = getLayoutY() + mouseEvent.getY() - startY;

            relocate(newX, newY);
        }
    }



    private City createCityNode(){

        HBox hBox = new HBox();
        VBox vBox = new VBox();

        hBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);

        Text cityName = new Text("City name");
        Text visited = new Text("Visited");
        CheckBox checkBoxVisited = new CheckBox();

        hBox.getChildren().addAll(visited,checkBoxVisited);
        vBox.getChildren().addAll(cityName, hBox);

        return new City("Paris");
    }
}
