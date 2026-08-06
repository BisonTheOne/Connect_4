package com.bart.connect4;

import com.bart.connect4.controller.GameController;
import com.bart.connect4.ui.BoardView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application{

    @Override
    public void start(Stage stage){

        BoardView boardView = new BoardView();

        new GameController(boardView);

        Scene scene = new Scene(boardView, 700, 600);

        stage.setTitle("Connect4");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}