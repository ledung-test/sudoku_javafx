package com.example.test.models;

public class Board {
    private Cell[][] cells;
    public Board() {
        cells = new Cell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new Cell();
            }
        }
    }
    public int[][] getBoard() {
        int[][] boardValues = new int[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                boardValues[i][j] = cells[i][j].getValue();
            }
        }
        return boardValues;
    }
    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

}
