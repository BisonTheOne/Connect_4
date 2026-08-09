package com.bart.connect4.controller;

import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;
import com.bart.connect4.ui.BoardView;

public class GameController {

    private Board board;
    private final BoardView boardView;
    private boolean gameOver;
    private Piece currentPlayer = Piece.RED;

    public GameController(BoardView boardView){
        this.boardView = boardView;
        this.board = new Board();

        boardView.setOnColumnClicked(this::playMove);
        boardView.setOnRestartClicked(this::restart);
        boardView.update(board);
        gameOver = false;

    }

    private void playMove(int column){
        int row = board.dropPiece(column,currentPlayer);
        if(row == -1)
            return;

        boardView.update(board);


        boolean finished = checkWin(board, row, column, currentPlayer);

        if(finished){
            this.gameOver = true;
            boardView.showWinner(currentPlayer);
            return;
        }

        currentPlayer =
                currentPlayer == Piece.RED ? Piece.YELLOW : Piece.RED;
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

            int count = 1; // the piece just placed counts as 1
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
