package com.bart.connect4.model;

public class Board {

    public static final int ROWS = 6;
    public static final int COLS = 7;

    public final Piece[][] grid = new Piece[ROWS][COLS];


    public Board() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = Piece.EMPTY;
            }
        }
    }

    public Piece getPiece(int row, int col) {
        return grid[row][col];
    }

    public boolean dropPiece(int column, Piece piece) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][column] == Piece.EMPTY) {
                grid[row][column] = piece;
                return true;
            }
        }
        return false;
    }
}
