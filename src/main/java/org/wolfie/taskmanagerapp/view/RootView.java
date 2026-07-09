package org.wolfie.taskmanagerapp.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RootView extends VBox {

    public RootView() {
        Label welcomeText = new Label();

        Button button = new Button("Hello world");
        button.setOnAction(e -> welcomeText.setText("Hello JavaFX"));

        setSpacing(20);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        getChildren().addAll(welcomeText, button);
    }
}