package com.bart.connect4.ai;

import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;


public class BotVsBotSimulator {

    public static void main(String[] args) {
        int numGames = 1000;

        MinimaxBot redBot = new MinimaxBot(Piece.RED,2);
        MinimaxBot yellowBot = new MinimaxBot(Piece.YELLOW);

        Results results = runGames(redBot, yellowBot, numGames);
        results.print(numGames);
    }


    public static Results runGames(Bot redBot, Bot yellowBot, int numGames) {
        Results results = new Results();

        for (int i = 0; i < numGames; i++) {
            Piece winner = playOneGame(redBot, yellowBot);

            if (winner == Piece.RED) {
                results.redWins++;
            } else if (winner == Piece.YELLOW) {
                results.yellowWins++;
            } else {
                results.draws++;
            }
        }

        return results;
    }


    private static Piece playOneGame(Bot redBot, Bot yellowBot) {
        Board board = new Board();
        Piece currentPlayer = Piece.RED;
        int movesPlayed = 0;
        int totalCells = Board.ROWS * Board.COLS;

        while (true) {
            Bot currentBot = (currentPlayer == Piece.RED) ? redBot : yellowBot;

            int column = currentBot.chooseColumn(board);
            int row = board.dropPiece(column, currentPlayer);

            if (row == -1) {

                continue;
            }

            movesPlayed++;

            if (checkWin(board, row, column, currentPlayer)) {
                return currentPlayer;
            }

            if (movesPlayed == totalCells) {
                return null; // draw, board is full
            }

            currentPlayer = (currentPlayer == Piece.RED) ? Piece.YELLOW : Piece.RED;
        }
    }


    private static boolean checkWin(Board board, int row, int col, Piece piece) {
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

    private static int countDirection(Board board, int row, int col, int dr, int dc, Piece piece) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS && board.getPiece(r, c) == piece) {
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }


    public static class Results {
        int redWins = 0;
        int yellowWins = 0;
        int draws = 0;

        public void print(int totalGames) {
            System.out.println("=== Bot vs Bot Results (" + totalGames + " games) ===");
            System.out.printf("Red wins:    %d (%.1f%%)%n", redWins, pct(redWins, totalGames));
            System.out.printf("Yellow wins: %d (%.1f%%)%n", yellowWins, pct(yellowWins, totalGames));
            System.out.printf("Draws:       %d (%.1f%%)%n", draws, pct(draws, totalGames));
        }

        private double pct(int count, int total) {
            return total == 0 ? 0.0 : (100.0 * count / total);
        }
    }
}