package com.bart.connect4.ai;
import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;

public interface Bot {
    int chooseColumn(Board board);
}
