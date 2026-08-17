package com.bart.connect4.controller;

import com.bart.connect4.ai.MinimaxBot;
import com.bart.connect4.ai.RandomBot;
import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;
import com.bart.connect4.ui.BoardView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GameController {

    private Board board;
    private final BoardView boardView;
    private boolean gameOver;
    private Piece currentPlayer = Piece.RED;
    private final MinimaxBot bot = new MinimaxBot(Piece.YELLOW);

    public GameController(BoardView boardView){
        this.boardView = boardView;
        this.board = new Board();

        boardView.setOnColumnClicked(this::playMove);
        boardView.setOnRestartClicked(this::restart);
        boardView.update(board);
        gameOver = false;

    }

    private void playMove(int column) {

        if (gameOver || currentPlayer != Piece.RED) {
            return;
        }

        boolean movePlayed = makeMove(column, Piece.RED);

        if (movePlayed && !gameOver) {
            playBotMove();
        }
    }

    private void playBotMove() {

        if (gameOver) {
            return;
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

        pause.setOnFinished(e->{
            if(gameOver)
                return;

            int column = bot.chooseColumn(board);

            makeMove(column, Piece.YELLOW);

        });

        pause.play();

    }

    private boolean makeMove(int column, Piece piece) {

        int row = board.dropPiece(column, piece);

        if (row == -1) {
            return false;
        }

        boardView.update(board);

        boolean finished = checkWin(board, row, column, piece);

        if (finished) {
            gameOver = true;
            boardView.showWinner(piece);
            return true;
        }

        currentPlayer =
                currentPlayer == Piece.RED
                        ? Piece.YELLOW
                        : Piece.RED;

        return true;
    }


    private boolean checkWin(Board board, int row, int col, Piece piece) {
        int[][] directions = {
                {0, 1},   // horizontal
                {1, 0},   // vertical
                {1, 1},   // diagonal ↘ / ↖
                {1, -1},  // diagonal ↙ / ↗
        };

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];

            int count = 1;
            count += countDirection(board, row, col, dr, dc, piece);
            count += countDirection(board, row, col, -dr, -dc, piece);

            if (count >= 4) {
                return true;
            }
        }

        return false;
    }

    private int countDirection(Board board, int row, int col, int dr, int dc, Piece piece){
        int count =  0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS && board.getPiece(r,c) == piece){
            count++;
            r+=dr;
            c+=dc;
        }
        return count;

    }

    public void restart(){
        board = new Board();
        currentPlayer = Piece.RED;
        gameOver = false;
        boardView.reset();
        boardView.update(board);
    }
}
