package com.bart.connect4.ui;

import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class BoardView extends Pane {

    private static final int CELL_SIZ = 100;
    private final Circle[][] cells = new Circle[Board.ROWS][Board.COLS];
    private Consumer<Integer> onColumnClicked;
    private final Label statusLabel;
    private final Rectangle overlay;
    private final Button restartButton;
    private Runnable onRestartClicked;

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
            clickArea.setTranslateX(col * CELL_SIZ);
            clickArea.setFill(Color.TRANSPARENT);

            final int column = col;

            clickArea.setOnMouseClicked(e -> {
                if (onColumnClicked != null) {
                    onColumnClicked.accept(column);
                }
            });
            getChildren().add(clickArea);
        }

        overlay = new Rectangle(Board.COLS * CELL_SIZ, Board.ROWS * CELL_SIZ);
        overlay.setFill(Color.rgb(0, 0, 0, 0.55));
        overlay.setVisible(false);
        getChildren().add(overlay);

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        statusLabel.setVisible(false);
        statusLabel.setLayoutX(Board.COLS * CELL_SIZ / 2.0 - 100);
        statusLabel.setLayoutY(Board.ROWS * CELL_SIZ / 2.0 - 25);
        getChildren().add(statusLabel);

        restartButton = new Button("Restart");
        restartButton.setVisible(false);
        restartButton.setLayoutX(Board.COLS * CELL_SIZ / 2.0 - 40);
        restartButton.setLayoutY(Board.ROWS * CELL_SIZ / 2.0 + 10);
        restartButton.setOnAction(e->{
            if(onRestartClicked != null)
                onRestartClicked.run();
        });
        getChildren().add(restartButton);
    }

    public void update(Board board) {
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

    public void showWinner(Piece piece) {
        statusLabel.setText(piece + " wins!");
        statusLabel.setVisible(true);
        overlay.setVisible(true);
        restartButton.setVisible(true);
    }

    public void reset() {
        statusLabel.setVisible(false);
        overlay.setVisible(false);
        restartButton.setVisible(false);
    }

    public void setOnColumnClicked(Consumer<Integer> callback) {
        this.onColumnClicked = callback;
    }

    public void setOnRestartClicked(Runnable callback){
        this.onRestartClicked = callback;
    }
}