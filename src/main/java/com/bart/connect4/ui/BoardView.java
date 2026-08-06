package com.bart.connect4.ui;

import com.bart.connect4.model.Board;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BoardView extends Pane {

    private static final int CELL = 100;
    private final Circle[][] cells = new Circle[6][7];

    public BoardView() {

        Rectangle background = new Rectangle(7 * CELL, 6 * CELL);
        background.setFill(Color.AQUAMARINE);

        getChildren().add(background);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {

                Circle hole = new Circle(CELL / 2.0 - 5);

                hole.setFill(Color.WHITE);

                hole.setCenterX(col * CELL + CELL / 2.0);
                hole.setCenterY(row * CELL + CELL / 2.0);

                cells[row][col] = hole;
                getChildren().add(hole);
            }
        }
    }

    public void update(Board board){
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {

                switch (board.getPiece(r, c)) {
                    case EMPTY -> cells[r][c].setFill(Color.WHITE);
                    case RED -> cells[r][c].setFill(Color.RED);
                    case YELLOW -> cells[r][c].setFill(Color.GOLD);
                }
            }
        }
    }
}