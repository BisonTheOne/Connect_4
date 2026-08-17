package com.bart.connect4.ai;

import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;

import java.util.Random;

public class RandomBot implements Bot {
    private final Random rnd = new Random();
    private final Piece piece;

    public RandomBot(Piece piece){
        this.piece = piece;
    }


    @Override
    public int chooseColumn(Board board) {

        int column ;

        do {
            column = rnd.nextInt(Board.COLS);
        }while (!board.isColumnAvailable(column));
            return column ;
        }



}
