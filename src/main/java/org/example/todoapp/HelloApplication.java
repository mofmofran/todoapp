package org.example.todoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {//extends=継承
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 460  , 520);
        stage.setTitle("ToDoリスト");
        stage.setScene(scene);
        stage.show();
    }
    public static  void main(String[]args){
        launch();
    }
}
