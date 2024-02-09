package com.tommycondon.ca1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("gui.fxml"));
        int width = 1200;
        int height = 1080;
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle("Pill and Capsule Analyser");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}