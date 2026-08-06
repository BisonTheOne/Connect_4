package com.bart.connect4.controller;

import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;
import com.bart.connect4.ui.BoardView;

public class GameController {

    private final Board board;
    private final BoardView boardView;

    private Piece currentPlayer = Piece.RED;

    public GameController(BoardView boardView){
        this.boardView = boardView;
        this.board = new Board();

        boardView.setOnColumnClicked(this::playMove);
        boardView.update(board);
    }

    private void playMove(int column){
        int row = board.dropPiece(column,currentPlayer);
        if(row == -1)
            return;

        boardView.update(board);

        currentPlayer =
                currentPlayer == Piece.RED ? Piece.YELLOW : Piece.RED;
    }
}
