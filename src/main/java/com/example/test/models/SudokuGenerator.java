package com.example.test.models;

import com.example.test.controllers.Message;
import com.example.test.enums.Difficulty;

import java.util.*;

public class SudokuGenerator {
    private Board board;
    private int[][] solutionBoard = new int[9][9];
    private boolean isSolvable;
    private List<Integer> possibleValues;
    private int solutionsCounter;
    private List<Coordinates> emptyTiles;
    private Difficulty currentDifficulty = Difficulty.EASY;
    int failedAttempts = 0;
    final int MAX_ATTEMPTS = 200;

    public SudokuGenerator() {
        board = new Board();
        emptyTiles = new ArrayList<>();
        getEmptyLocations();
        solutionsCounter = 0;
        isSolvable = false;
        possibleValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }
    //Restart các trường của lớp
    public void reset() {
        board = new Board();
        emptyTiles.clear();
        getEmptyLocations();
        solutionsCounter = 0;
        isSolvable = false;
        possibleValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        currentDifficulty = Difficulty.EASY;
        solutionBoard = new int[9][9];
    }
    private void printBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board.getCell(i, j).getValue() + " ");
            }
            System.out.println();
        }
        System.out.println("----------");
    }
    //Lấy board đã xóa các ô
    public int[][] getBoardRemove(){
        clearArray(board.getBoard());
        emptyTiles.clear();
        getEmptyLocations();
        solutionsCounter = 0;
        fillBoard(0);
        removeTilesFromTheBoard(currentDifficulty.getRemovals());
        return board.getBoard();
    }
    //Lấy ra bảng kết quả
    public int[][] getSolutionBoard() {
        return solutionBoard;
    }
    //Set độ khó game
    public void setCurrentDifficulty(Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }
    //Lấy ra bảng board đã xóa
    public Board getBoard() {
        return this.board;
    }
    //Lấy giá trị người dùng chọn so sánh với ô trong bảng kết quả
    public boolean isUserInputValid(int x, int y, int userInput) {
        return solutionBoard[x][y] == userInput;
    }
    //Lấy kết quả từ bảng kết quả với vị trí cụ thể
    public int getSolutionValue(int x, int y){
        return solutionBoard[x][y];
    }
    //Kiểm tra bảng người dùng điền trùng với bảng kết quả
    public boolean isBoardComplete() {
        int[][] currentPlayerBoard = board.getBoard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (currentPlayerBoard[i][j] != solutionBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    //Lấy tất cả vị trí các phần tử có giá trị bằng 0 trong board
    private void getEmptyLocations(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board.getCell(i, j).getValue() == 0)
                {
                    emptyTiles.add(new Coordinates(i,j));
                }
            }
        }
    }
    //Kiểm tra số lần xuất hiện của 1 giá trị trong 1 mảng
    private boolean isValueDuplicated(int value, int[] frequencies) {
        if (value != 0) {
            frequencies[value]++;
            if (frequencies[value] > 1) {
                return true;
            }
        }
        return false;
    }
    //Kiểm tra trùng lặp ô 3x3
    private boolean checkBox(int rowStart, int columnStart) {
        int[] frequencies = new int[10];
        for (int xIndex = rowStart; xIndex < rowStart + 3; xIndex++) {
            for (int yIndex = columnStart; yIndex < columnStart + 3; yIndex++) {
                int value = board.getCell(xIndex, yIndex).getValue();
                if (isValueDuplicated(value, frequencies)) {
                    return false;
                }
            }
        }
        return true;
    }
    //Kiểm tra xem đang kiểm tra trên hàng hay cột
    private boolean checkSequence(int index, boolean isRow) {
        int[] frequencies = new int[10];
        for (int i = 0; i < 9; i++) {
            int value;
            if (isRow) {
                value = board.getCell(index, i).getValue();
            } else {
                value = board.getCell(i, index).getValue();
            }
            if (isValueDuplicated(value, frequencies)) {
                return false;
            }
        }
        return true;
    }
    //Kiểm tra trùng lặp trên hàng
    private boolean checkRow(int row) {
        return checkSequence(row, true);
    }
    //Kiểm tra trùng lặp trên cột
    private boolean checkColumn(int column) {
        return checkSequence(column, false);
    }
    public void printSolutionBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(solutionBoard[i][j] + " ");
            }
            System.out.println();
        }
    }
    //Dùng backtracking tạo ra một bảng hợp lệ
    private void fillBoard(int currentTileNr){
        Collections.shuffle(possibleValues);//Xáo trộn các số để mỗi lần chạy thuật toán các số được thử ngẫu nhiên
        for (int index = 0; index < possibleValues.size() && !isSolvable; index++) {
            int xIndex = emptyTiles.get(currentTileNr).getX();
            int yIndex = emptyTiles.get(currentTileNr).getY();
            board.getCell(xIndex, yIndex).setValue(possibleValues.get(index));
            if (checkRow(xIndex) &&
                    checkColumn(yIndex) &&
                    checkBox((xIndex/3) * 3, (yIndex/3) * 3)) {
                //printCurrentBoard();
                if (currentTileNr + 1 == emptyTiles.size()) {
                    isSolvable = true;
                    solutionBoard = copySudokuBoard(board.getBoard());
                }
                else{
                    fillBoard(currentTileNr + 1);
                }
            }
            if (!isSolvable)
                board.getCell(xIndex, yIndex).setValue(0);
        }
    }
    //Đếm số lời giải có thể có cho một bảng Sudoku
    private void countSolutions(int currentTileNr) {
        if (currentTileNr == emptyTiles.size()) {
            solutionsCounter++;
            return;
        }
        int xIndex = emptyTiles.get(currentTileNr).getX();
        int yIndex = emptyTiles.get(currentTileNr).getY();
        for (int num = 1; num <= 9; num++) {
            if (isValidMove(xIndex, yIndex, num)) {
                board.getCell(xIndex, yIndex).setValue(num);
                countSolutions(currentTileNr + 1);
                board.getCell(xIndex, yIndex).setValue(0);
            }
        }
    }
    //Kiểm tra giá trị số đặt vào có thỏa mãn luật chơi không
    private boolean isValidMove(int x, int y, int num) {
        int prevValue = board.getCell(x, y).getValue();  // Lưu giá trị cũ
        board.getCell(x, y).setValue(num);  // Đặt giá trị mới để kiểm tra
        boolean valid = checkRow(x) && checkColumn(y) && checkBox(x - x % 3, y - y % 3);
        board.getCell(x, y).setValue(prevValue);  // Khôi phục giá trị cũ sau khi kiểm tra
        return valid;
    }
    //Thực hiện xóa một số ô đi
    private void removeTilesFromTheBoard(int nrOfRemovals){
        ArrayList<Coordinates> removableTiles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getCell(i, j).getValue() != 0) {
                    removableTiles.add(new Coordinates(i, j));
                }
            }
        }
        Random random = new Random(System.currentTimeMillis());
        while(nrOfRemovals > 0 && !removableTiles.isEmpty()){
            Coordinates randomTile = removableTiles.get(random.nextInt(removableTiles.size()));
            //Lấy tọa độ của 2 ô đối xứng
            int xCoord = randomTile.getX();
            int yCoord = randomTile.getY();
            int symmetricXCoord = 8 - xCoord;
            int symmetricYCoord = 8 - yCoord;
            //Tạo một biến backup lưu giá trị của 2 ô
            int backUpValue = board.getCell(xCoord, yCoord).getValue();
            int symmetricBackUpValue = board.getCell(symmetricXCoord, symmetricYCoord).getValue();
            //Đặt giá trị 2 ô đối xứng là 0 = xóa ô đó
            board.getCell(xCoord, yCoord).setValue(0);
            board.getCell(symmetricXCoord, symmetricYCoord).setValue(0);
            //Lấy lại vị trí các ô bằng 0
            emptyTiles.clear();
            getEmptyLocations();
            //Thực hiện đếm số lời giải
            solutionsCounter = 0;
            countSolutions(0);
            //Nếu số lời giải khác 1 thì trả lại giá trị cho 2 ô, bằng 1 thì thực hiện xóa ô
            if (solutionsCounter != 1) {
                board.getCell(xCoord, yCoord).setValue(backUpValue);
                board.getCell(symmetricXCoord, symmetricYCoord).setValue(symmetricBackUpValue);
                System.out.println(backUpValue);
                System.out.println(symmetricBackUpValue);
                failedAttempts++;
                if (failedAttempts >= MAX_ATTEMPTS) {
                    Message.showAlert("Thông báo", "Không thể tạo ra một bảng Sudoku với giải pháp duy nhất. " +
                            "Bảng dưới đây có thể có nhiều cách giải.");
                    failedAttempts = 0;
                    return;
                }
            }else {
                nrOfRemovals--;
                System.out.println("Số cách: " + solutionsCounter);
                removableTiles.removeIf(tile -> tile.getX() == xCoord && tile.getY() == yCoord);
                removableTiles.removeIf(tile -> tile.getX() == symmetricXCoord && tile.getY() == symmetricYCoord);
            }
        }
    }

    //Clear một mảng
    public static void clearArray(int[][] board) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                board[xIndex][yIndex] = 0;
            }
        }
    }
    //Copy một mảng
    public static int[][] copySudokuBoard(int[][] oldBoard){
        int[][] copiedBoard = new int[9][9];
        for (int xValue = 0; xValue < 9; xValue++) {
            System.arraycopy(oldBoard[xValue], 0, copiedBoard[xValue], 0, 9);
        }
        return copiedBoard;
    }

}
