package se.su.inlupp;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GuiRail extends BorderPane {
    private GuiCity  city1;
    private GuiCity city2;

    private Text railNameTag;
    private Text railWeightTag;

    private Line railLine = new Line();
    VBox vbox = new VBox();

    public GuiRail(GuiCity city1, GuiCity city2, String railNameTag, int railWeightTag){
        this.city1 = city1;
        this.city2 = city2;
        this.railNameTag = new Text(railNameTag);
        this.railWeightTag = new Text(railWeightTag + "km");

        vbox.getChildren().addAll(this.railNameTag, this.railWeightTag);
        vbox.setAlignment(Pos.CENTER);

        railLine.startXProperty().bind(city1.layoutXProperty().add(city1.widthProperty().divide(2)));
        railLine.startYProperty().bind(city1.layoutYProperty().add(city1.heightProperty().divide(2)));
        railLine.endXProperty().bind(city2.layoutXProperty().add(city2.widthProperty().divide(2)));
        railLine.endYProperty().bind(city2.layoutYProperty().add(city2.heightProperty().divide(2)));

        vbox.layoutYProperty().bind(railLine.startYProperty().add(railLine.endYProperty()).divide(2).subtract(40));
        vbox.layoutXProperty().bind(railLine.startXProperty().add(railLine.endXProperty()).divide(2).subtract(25));

        railLine.setStroke(Color.RED);
        railLine.setStrokeWidth(3);
    }

    public GuiCity getCity1() {
        return city1;
    }

    public GuiCity getCity2() {
        return city2;
    }

    public Line getLine() {
        return railLine;
    }

    public VBox getVBox() {
        return vbox;
    }
}
