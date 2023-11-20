package test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Test extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("JavaFX Example");

        GridPane gridPane = createGridPane();

        Button openPopupButton = new Button("Open Popup");
        openPopupButton.setOnAction(e -> openPopup());

        gridPane.add(openPopupButton, 1, 1);

        StackPane root = new StackPane();
        root.getChildren().add(gridPane);
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);
        return gridPane;
    }

    private void openPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Popup");

        GridPane popupGridPane = createGridPane();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();

        Button validateButton = new Button("Validate");
        validateButton.setOnAction(e -> {
            // Add logic to update the main gridPane with entered information
            updateMainGridPane(nameField.getText(), ageField.getText());
            popupStage.close();
        });

        popupGridPane.add(nameLabel, 0, 0);
        popupGridPane.add(nameField, 1, 0);
        popupGridPane.add(ageLabel, 0, 1);
        popupGridPane.add(ageField, 1, 1);
        popupGridPane.add(validateButton, 1, 2);

        Scene popupScene = new Scene(popupGridPane, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private void updateMainGridPane(String name, String age) {
        // Add logic to update the main gridPane with the entered information
        Label nameLabel = new Label("Name: " + name);
        Label ageLabel = new Label("Age: " + age);

        GridPane mainGridPane = (GridPane) ((StackPane) primaryStage.getScene().getRoot()).getChildren().get(0);
        System.out.println(((StackPane) primaryStage.getScene().getRoot()).getChildren());

        mainGridPane.add(nameLabel, 0, 2);
        mainGridPane.add(ageLabel, 1, 2);
    }
}
