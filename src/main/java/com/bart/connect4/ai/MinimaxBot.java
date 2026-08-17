package com.bart.connect4.ai;

import com.bart.connect4.model.Board;
import com.bart.connect4.model.Piece;

public class MinimaxBot implements Bot{

    private final Piece playerPiece;
    private final Piece opponentPiece;
    private final int maxDepth;

    public MinimaxBot(Piece playerPiece, int maxDepth){
        this.maxDepth = maxDepth;
        this.playerPiece = playerPiece;
        this.opponentPiece = (playerPiece == Piece.RED) ? Piece.YELLOW : Piece.RED;
    }

    public MinimaxBot(Piece playerPiece){
        this(playerPiece,5);
    }

    public int chooseColumn(Board board) {
        Piece[][] grid = copyGrid(board);

        int bestColumn = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int col : columnOrder()) {
            if (!board.isColumnAvailable(col)) {
                continue;
            }

            int row = dropInGrid(grid, col, playerPiece);
            int score;

            if (isWinningMove(grid, row, col, playerPiece)) {
                score = 1_000_000; // immediate win, take it
            } else {
                score = minimax(grid, maxDepth - 1, alpha, beta, false);
            }

            undoInGrid(grid, row, col);

            if (score > bestScore) {
                bestScore = score;
                bestColumn = col;
            }
            alpha = Math.max(alpha, bestScore);
        }


        if (bestColumn == -1) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.isColumnAvailable(col)) {
                    return col;
                }
            }
        }

        return bestColumn;
    }

    private int minimax(Piece[][] grid, int depth, int alpha, int beta, boolean maximizing) {
        if (depth == 0 || isBoardFull(grid)) {
            return evaluateBoard(grid);
        }

        Piece piece = maximizing ? playerPiece : opponentPiece;

        if (maximizing) {
            int best = Integer.MIN_VALUE;
            for (int col : columnOrder()) {
                if (!isColumnAvailable(grid, col)) continue;

                int row = dropInGrid(grid, col, piece);

                int score;
                if (isWinningMove(grid, row, col, piece)) {
                    score = 1_000_000 - (maxDepth - depth); // prefer faster wins
                } else {
                    score = minimax(grid, depth - 1, alpha, beta, false);
                }

                undoInGrid(grid, row, col);

                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (alpha >= beta) break; // prune
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int col : columnOrder()) {
                if (!isColumnAvailable(grid, col)) continue;

                int row = dropInGrid(grid, col, piece);

                int score;
                if (isWinningMove(grid, row, col, piece)) {
                    score = -1_000_000 + (maxDepth - depth); // prefer slower losses
                } else {
                    score = minimax(grid, depth - 1, alpha, beta, true);
                }

                undoInGrid(grid, row, col);

                best = Math.min(best, score);
                beta = Math.min(beta, best);
                if (alpha >= beta) break; // prune
            }
            return best;
        }
    }


    private int[] columnOrder() {
        int[] order = new int[Board.COLS];
        int mid = Board.COLS / 2;
        int idx = 0;
        order[idx++] = mid;
        for (int offset = 1; offset <= Board.COLS; offset++) {
            if (mid - offset >= 0) order[idx++] = mid - offset;
            if (mid + offset < Board.COLS) order[idx++] = mid + offset;
            if (idx >= Board.COLS) break;
        }
        return order;
    }



    private Piece[][] copyGrid(Board board) {
        Piece[][] grid = new Piece[Board.ROWS][Board.COLS];
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                grid[r][c] = board.getPiece(r, c);
            }
        }
        return grid;
    }

    private boolean isColumnAvailable(Piece[][] grid, int col) {
        return grid[0][col] == Piece.EMPTY;
    }

    private int findAvailableRow(Piece[][] grid, int col) {
        for (int r = Board.ROWS - 1; r >= 0; r--) {
            if (grid[r][col] == Piece.EMPTY) {
                return r;
            }
        }
        return -1;
    }

    private int dropInGrid(Piece[][] grid, int col, Piece piece) {
        int row = findAvailableRow(grid, col);
        grid[row][col] = piece;
        return row;
    }

    private void undoInGrid(Piece[][] grid, int row, int col) {
        grid[row][col] = Piece.EMPTY;
    }

    private boolean isBoardFull(Piece[][] grid) {
        for (int c = 0; c < Board.COLS; c++) {
            if (grid[0][c] == Piece.EMPTY) return false;
        }
        return true;
    }



    private boolean isWinningMove(Piece[][] grid, int row, int col, Piece piece) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;
            count += countDirection(grid, row, col, dir[0], dir[1], piece);
            count += countDirection(grid, row, col, -dir[0], -dir[1], piece);
            if (count >= 4) return true;
        }
        return false;
    }

    private int countDirection(Piece[][] grid, int row, int col, int dr, int dc, Piece piece) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS && grid[r][c] == piece) {
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }



    private int evaluateBoard(Piece[][] grid) {
        int score = 0;


        int centerCol = Board.COLS / 2;
        for (int r = 0; r < Board.ROWS; r++) {
            if (grid[r][centerCol] == playerPiece) score += 3;
            else if (grid[r][centerCol] == opponentPiece) score -= 3;
        }


        score += scoreDirection(grid, 0, 1);
        score += scoreDirection(grid, 1, 0);
        score += scoreDirection(grid, 1, 1);
        score += scoreDirection(grid, 1, -1);

        return score;
    }

    private int scoreDirection(Piece[][] grid, int dr, int dc) {
        int total = 0;
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                int endR = r + dr * 3;
                int endC = c + dc * 3;
                if (endR < 0 || endR >= Board.ROWS || endC < 0 || endC >= Board.COLS) continue;

                Piece[] window = new Piece[4];
                for (int i = 0; i < 4; i++) {
                    window[i] = grid[r + dr * i][c + dc * i];
                }
                total += scoreWindow(window);
            }
        }
        return total;
    }

    private int scoreWindow(Piece[] window) {
        int myCount = 0, oppCount = 0, emptyCount = 0;
        for (Piece p : window) {
            if (p == playerPiece) myCount++;
            else if (p == opponentPiece) oppCount++;
            else emptyCount++;
        }

        if (myCount > 0 && oppCount > 0) return 0;

        if (myCount == 4) return 100000;
        if (myCount == 3 && emptyCount == 1) return 100;
        if (myCount == 2 && emptyCount == 2) return 10;

        if (oppCount == 4) return -100000;
        if (oppCount == 3 && emptyCount == 1) return -120; // weight blocking slightly higher
        if (oppCount == 2 && emptyCount == 2) return -10;

        return 0;
    }
}
