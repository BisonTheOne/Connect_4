package com.bart.connect4.ui;

import com.bart.connect4.model.Board;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class BoardView extends Pane {

    private static final int CELL_SIZ = 100;
    private final Circle[][] cells = new Circle[Board.ROWS][Board.COLS];
    private Consumer<Integer> onColumnClicked;


    public BoardView() {

        Rectangle background = new Rectangle(Board.COLS * CELL_SIZ, Board.ROWS * CELL_SIZ);
        background.setFill(Color.AQUAMARINE);

        getChildren().add(background);


        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {



                Circle hole = new Circle(CELL_SIZ / 2.0 - 5);

                hole.setFill(Color.WHITE);

                hole.setCenterX(col * CELL_SIZ + CELL_SIZ / 2.0);
                hole.setCenterY(row * CELL_SIZ + CELL_SIZ / 2.0);

                cells[row][col] = hole;
                getChildren().add(hole);
            }
        }
        for (int col = 0; col < Board.COLS; col++) {

            Rectangle clickArea = new Rectangle(CELL_SIZ, CELL_SIZ * Board.ROWS);

            clickArea.setTranslateX(col* CELL_SIZ);

            clickArea.setFill(Color.TRANSPARENT);

            final int column = col;

            clickArea.setOnMouseClicked(e->{
                if(onColumnClicked!=null)
                {
                    onColumnClicked.accept(column);
                }
            });
            getChildren().add(clickArea);}
    }

    public void update(Board board){
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {

                switch (board.getPiece(r, c)) {
                    case EMPTY -> cells[r][c].setFill(Color.WHITE);
                    case RED -> cells[r][c].setFill(Color.RED);
                    case YELLOW -> cells[r][c].setFill(Color.GOLD);
                }
            }
        }
    }

    public void setOnColumnClicked(Consumer<Integer> callback){
        this.onColumnClicked=callback;
    }
}