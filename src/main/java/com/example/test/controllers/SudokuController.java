package com.example.test.controllers;

import com.example.test.enums.Difficulty;
import com.example.test.models.Cell;
import com.example.test.models.Coordinates;
import com.example.test.models.SudokuGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.net.URL;
import java.util.ResourceBundle;

public class SudokuController implements Initializable {
    @FXML
    private GridPane sudokuGrid, numberGrid;
    @FXML
    private Label label_note, label_suggest, time_label, error_lb, label_easy, label_medium, label_hard;
    @FXML
    private SVGPath toggleIcon;

    private boolean isPaused = false;
    private int errorCount = 0;
    private int suggestCount = 3;
    private boolean checkNote = false;

    private SudokuGenerator sudokuGenerator;
    private Message message;
    private StackPane selectedCell = null;
    private Time time;
    private Difficulty difficulty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIEvents();
        initializeGame();
        printBoardInConsole();
        loadBoardValuesToUI();
        time.initTimer();
        time.printTimer(time_label);
    }
    //In bảng đã xóa các ô và bảng lời giải ra console
    private void printBoardInConsole(){
        System.out.println("Bảng xóa các ô");
        sudokuGenerator.getBoard().printBoard(sudokuGenerator.getBoardRemove());
        System.out.println("Bảng lời giả");
        sudokuGenerator.printSolutionBoard();
    }
    //Khởi tạo các đối tượng
    private void initializeGame() {
        sudokuGenerator = new SudokuGenerator();
        time = new Time();
        message = new Message(this, time);
    }
    //Gán các sự kiện cho thành phần ui
    private void initializeUIEvents() {
        setupDifficultyLabels();
        setupCellClickEvent();
        setupNumberButtonEvents();
    }
    //Mặc định độ khó khi bắt đầu game
    private void setupDifficultyLabels() {
        label_easy.getStyleClass().add("difficult_label_selected");
        difficulty = Difficulty.EASY;
    }
    //=======================================GÁN SỰ KIỆN CHO CÁC THÀNH PHẦN UI==========================================
    //Gán sự kiện handleCellClick cho tất cả stack pane thuộc sudokuGrid
    private void setupCellClickEvent() {
        for (Node cell : sudokuGrid.getChildren()) {
            if (cell instanceof StackPane) {
                cell.setOnMouseClicked(this::handleCellClick);
            }
        }
    }
    //Gán sự kiện handleNumberButton cho tất cả các button thuộc numberGird
    private void setupNumberButtonEvents() {
        for (Node node : numberGrid.getChildren()) {
            if (node instanceof Button) {
                Button numberButton = (Button) node;
                numberButton.setOnAction(this::handleNumberButton);
            }
        }
    }
    //==================================================================================================================
    //===========================================CHỌN ĐỘ KHÓ GAME=======================================================
    //Thực hiện chọn độ khó game và tạo game mới
    private void handleDifficultySelection(Difficulty newDifficulty) {
        label_easy.getStyleClass().remove("difficult_label_selected");
        label_medium.getStyleClass().remove("difficult_label_selected");
        label_hard.getStyleClass().remove("difficult_label_selected");
        difficulty = newDifficulty;
        switch (difficulty) {
            case EASY:
                label_easy.getStyleClass().add("difficult_label_selected");
                break;
            case MEDIUM:
                label_medium.getStyleClass().add("difficult_label_selected");
                break;
            case HARD:
                label_hard.getStyleClass().add("difficult_label_selected");
                break;
        }
        restartGame();
    }
    //Chọn chế độ dễ
    public void setupDifficultyEasy(MouseEvent event){
        if(message.showConfirmationDifficulty()){
            handleDifficultySelection(Difficulty.EASY);
        }
    }
    //Chọn chế độ bình thường
    public void setupDifficultyMedium(MouseEvent event){
        if (message.showConfirmationDifficulty()){
            handleDifficultySelection(Difficulty.MEDIUM);
        }
    }
    //Chọn chế độ khó
    public void setupDifficultyHard(MouseEvent event){
        if (message.showConfirmationDifficulty()){
            handleDifficultySelection(Difficulty.HARD);
        }
    }
    //==================================================================================================================
    //========================================XỬ LÝ CLICK MỘT Ô TRÊN GRID PANE 9X9======================================
    //Sự kiện click một ô trên sudokuGrid
    public void handleCellClick(MouseEvent event) {
        //Thực hiện xóa css trên ô đã chọn trước đó
        if (selectedCell != null) {
            for (Node cell : sudokuGrid.getChildren()) {
                if (cell instanceof StackPane) {
                    cell.getStyleClass().remove("highlighted-related-cell");
                    cell.getStyleClass().remove("highlighted-same-value");
                    cell.getStyleClass().remove("highlighted-selected-cell");
                }
            }
        }
        selectedCell = (StackPane) event.getSource(); //Lấy vị trí ô
        selectedCell.getStyleClass().add("highlighted-selected-cell");
        Label targetLabel = getLabelFromStackPane(selectedCell);
        //Tạo highligh cho các ô có cùng giá trị với ô đã chọn
        String value = targetLabel.getText();
        for (Node cell : sudokuGrid.getChildren()) {
            if (cell instanceof StackPane) {
                Label label = getLabelFromStackPane((StackPane) cell);
                if (!"0".equals(label.getText().trim()) && !"".equals(label.getText().trim()) && value.equals(label.getText())) {
                    cell.getStyleClass().add("highlighted-same-value");
                }
            }
        }
    }
    //==================================================================================================================
    //========================================XỬ LÝ CLCIK CHỌN 1 GIÁ TRỊ TỪ 1 ĐẾN 9=====================================
    //Lấy giá trị số từ 1 đến 9 khi chọn từ numberGrid
    @FXML
    public void handleNumberButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int number = Integer.parseInt(clickedButton.getText());
        if (selectedCell == null) return;
        Label targetLabel = getLabelFromStackPane(selectedCell);
        if (targetLabel == null) return;
        Coordinates coords = getGridCoordinates(selectedCell);
        Cell currentCell = sudokuGenerator.getBoard().getCell(coords.getX(), coords.getY());
        if (checkNote && currentCell.isEditable() ) {
            updateNoteInGrid(selectedCell, number);
        } else {
            Label mainLabel = getLabelFromStackPane(selectedCell);
            if (selectedCell != null && currentCell.isEditable()) {
                handleInputMode(number, mainLabel, coords);
                clearNotesFromStackPane(selectedCell);
            } else {
                System.out.println("Ô này không thể chỉnh sửa.");
            }
        }
    }
    //Xử lý khi nhập đúng hoặc sai một ô
    private void handleInputMode(int number, Label label, Coordinates coords) {
        label.setText(String.valueOf(number));
        if (sudokuGenerator.isUserInputValid(coords.getX(), coords.getY(), number)) {
            label.setTextFill(Color.BLACK);
            if (areAllLabelsFilled(sudokuGrid)){
                loadBoardValuesFromUI();
                if (sudokuGenerator.isBoardComplete()) {
                    message.showSuccessMessage();
                }
            }
        } else {
            label.setTextFill(Color.RED);
            errorCount++;
            error_lb.setText("Lỗi: " + errorCount + "/3");
            if (errorCount == 3) {
                time.pauseTimer();
                if (message.showAlertGameOver()){
                    errorCount--;
                    error_lb.setText("Lỗi: " + errorCount + "/3");
                    time.startTimer();
                }else {
                    restartGame();
                    time.stopTimer();
                    time.startTimer();
                }

            }
        }
    }
    //==================================================================================================================
    //==================================XỬ LÝ CẬP NHẬT GIÁ TRỊ TỪ BOARD RA UI VÀ NGƯỢC LẠI==============================
    //Lấy giá trị từ Grid Pane vào board
    public void loadBoardValuesToUI() {
        for (Node stackPaneNode : sudokuGrid.getChildren()) {
            if (stackPaneNode instanceof StackPane) {
                StackPane cellPane = (StackPane) stackPaneNode;
                Coordinates coord = getGridCoordinates(cellPane);
                Cell cell = sudokuGenerator.getBoard().getCell(coord.getX(), coord.getY());
                Label label = getLabelFromStackPane(cellPane);
                if (cell.getValue() != 0){
                    label.setText(String.valueOf(cell.getValue()));
                }else {
                    label.setText("");
                }
            }
        }
    }
    //Lấy giá trị từ board đẩy ra Grid Pane
    public void loadBoardValuesFromUI() {
        for (Node stackPaneNode : sudokuGrid.getChildren()) {
            if (stackPaneNode instanceof StackPane) {
                StackPane cellPane = (StackPane) stackPaneNode;
                Coordinates coord = getGridCoordinates(cellPane);
                Label label = getLabelFromStackPane(cellPane);
                String text = label.getText();
                if (!text.trim().isEmpty()) {
                    sudokuGenerator.getBoard().getCell(coord.getX(), coord.getY()).setValue(Integer.parseInt(text));
                }
            }
        }
    }
    private void updateCellUIFromSolution(int row, int col, int solutionValue) {
        Coordinates coordinates = new Coordinates(row, col);
        StackPane correspondingStackPane = getStackPaneFromCoordinates(coordinates);
        Label mainLabel = getLabelFromStackPane(correspondingStackPane);
        // Xóa ghi chú (nếu có)
        GridPane notesGrid = getNotesGridFromStackPane(correspondingStackPane);
        for (Node node : notesGrid.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setText("");
            }
        }
        // Đặt giá trị từ solutionValue vào label
        if (mainLabel != null) {
            mainLabel.setText(String.valueOf(solutionValue));
        }
    }

    //==================================================================================================================
    //=======================================RESTART GAME===============================================================
    //Restart game
    public void restartGame() {
        System.out.println(difficulty);
        resetTimer();
        resetErrorsAndHints();
        restartPause();
        resetSelectedCell();
        resetCellColors();
        resetNotes();
        restartVisibleStackPane();
        resetGameBoard();
        loadBoardValuesToUI();
    }
    //Restart time
    private void resetTimer() {
        time.restartTimer();
        time.printTimer(time_label);
    }
    //Restart lỗi và gợi ý
    private void resetErrorsAndHints() {
        error_lb.setText("Lỗi: 0/3");
        errorCount = 0;
        suggestCount = 3;
        label_suggest.setText("Gợi ý: " + suggestCount +"/3");
    }
    //Restart ô đã chọn
    private void resetSelectedCell() {
        if (selectedCell != null) {
            for (Node cell : sudokuGrid.getChildren()) {
                if (cell instanceof StackPane) {
                    cell.getStyleClass().remove("highlighted-same-value");
                    cell.getStyleClass().remove("highlighted-selected-cell");
                }
            }
            selectedCell = null;
        }
    }
    //Restart màu số các ô đã điền
    private void resetCellColors() {
        for (Node cell : sudokuGrid.getChildren()) {
            if (cell instanceof StackPane) {
                for (Node child : ((StackPane) cell).getChildren()) {
                    if (child instanceof Label) {
                        ((Label) child).setTextFill(Color.BLACK);
                    }
                }
            }
        }
    }
    //Restart các ô chứa node về trống
    private void resetNotes() {
        for (Node cell : sudokuGrid.getChildren()) {
            if (cell instanceof StackPane) {
                clearNotesFromStackPane((StackPane) cell);
            }
        }
    }
    //Restart SudokuGenerator
    private void resetGameBoard() {
        sudokuGenerator.reset();
        sudokuGenerator.setCurrentDifficulty(difficulty);
        sudokuGrid.getStyleClass().remove("paused-border");
        printBoardInConsole();
    }
    //==================================================================================================================
    //====================================CHỨC NĂNG GHI CHÚ=============================================================
    //Ghi chú
    @FXML
    public void handleNoteSelection(ActionEvent event){
        if (checkNote){
            checkNote = false;
            label_note.setText("Ghi chú: Tắt");
        }else {
            checkNote = true;
            label_note.setText("Ghi chú: Bật");
        }
    }
    //Thực hiện cập nhật các ghi chú vào grid pane 3x3
    private void updateNoteInGrid(StackPane stackPane, int number) {
        GridPane notesGrid = getNotesGridFromStackPane(stackPane);
        if (notesGrid == null) return;
        // Xác định vị trí label dựa trên số từ 1-9
        int row = (number - 1) / 3;
        int col = (number - 1) % 3;
        // Duyệt qua các label trong GridPane để tìm label phù hợp và cập nhật giá trị
        for (Node child : notesGrid.getChildren()) {
            Coordinates coords = getGridCoordinates(child);
            if (coords.getX() == row && coords.getY() == col && child instanceof Label) {
                Label noteLabel = (Label) child;
                if (noteLabel.getText().equals(String.valueOf(number))) {
                    noteLabel.setText(""); // Xóa ghi chú
                } else {
                    Label mainLabel = getLabelFromStackPane(stackPane);
                    if (mainLabel != null) {
                        mainLabel.setText("");
                    }
                    noteLabel.setText(String.valueOf(number)); // Thêm ghi chú
                }
                break;
            }
        }
    }
    //Xóa các ghi chú
    private void clearNotesFromStackPane(StackPane stackPane) {
        GridPane notesGrid = getNotesGridFromStackPane(stackPane);
        if (notesGrid == null) return;
        // Duyệt qua tất cả các label trong GridPane và xóa giá trị của chúng
        for (Node child : notesGrid.getChildren()) {
            if (child instanceof Label) {
                ((Label) child).setText("");
            }
        }
    }
    //==================================================================================================================
    //================================CHỨC NĂNG GỢI Ý===================================================================
    //Gợi ý
    @FXML
    public void handleSuggestSelection(ActionEvent event) {
        if (suggestCount > 0) {
            provideHint();
            suggestCount--;
            label_suggest.setText("Gợi ý: " + suggestCount + "/3");
        } else {
            message.showSuggestCount(); // thông báo đã hết số lần gợi ý
        }
    }
    //Điền gợi ý vào một ô trống trên board
    public void provideHint() {
        resetCellColors();
        boolean isHintProvided = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = sudokuGenerator.getBoard().getCell(i, j);
                if (cell.getValue() == 0) {
                    int solutionValue = sudokuGenerator.getSolutionValue(i, j);
                    cell.setValue(solutionValue);
                    updateCellUIFromSolution(i,j, solutionValue);
                    isHintProvided = true;
                    break;
                }
            }
            if (isHintProvided) {
                break;
            }
        }
        // Kiểm tra xem bảng Sudoku đã hoàn thành chưa sau khi cung cấp gợi ý.
        if (isHintProvided && sudokuGenerator.isBoardComplete()) {
            message.showSuccessMessage();
        }
    }
    //==================================================================================================================
    //====================================CHỨC NĂNG TẠM DỪNG============================================================
    //Xử lý khi thực hiện pause
    @FXML
    private void handlePauseSelection(ActionEvent event){
        if (isPaused) {
            // Đổi sang biểu tượng pause và tiếp tục hoạt động
            toggleIcon.setContent("M10 5v24h-6V5h6zM18 5v24h-6V5h6z");
            isPaused = false;
            for (Node node : sudokuGrid.getChildren()) {
                if (node instanceof StackPane) {
                    node.setVisible(!isPaused);
                }
            }
            sudokuGrid.getStyleClass().remove("paused-border");
            time.startTimer();
        } else {
            // Đổi sang biểu tượng play và tạm dừng
            toggleIcon.setContent("M5 5v24l18-12L5 5z"); // Biểu tượng play
            isPaused = true;
            for (Node node : sudokuGrid.getChildren()) {
                if (node instanceof StackPane) {
                    node.setVisible(!isPaused);
                }
            }
            sudokuGrid.getStyleClass().add("paused-border");
            time.pauseTimer();
        }
    }
    //Restart đưa về icon pause
    private void restartPause(){
        toggleIcon.setContent("M10 5v24h-6V5h6zM18 5v24h-6V5h6z");
        isPaused = false;
    }
    //Đưa stack pane về trạng thái hiện thị
    private void restartVisibleStackPane(){
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof StackPane) {
                node.setVisible(true);
            }
        }
    }
    //==================================================================================================================
    //=========================================HIỆN THỊ KẾT QUẢ GIẢI====================================================
    //Hiện thị kết quả giải
    @FXML
    private void showSolution(ActionEvent event){
        if (message.showConfirmationSolution()){
            int[][] solutionBoard = sudokuGenerator.getSolutionBoard();
            // Duyệt qua toàn bộ bảng Sudoku và cập nhật giá trị từ solutionBoard
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    updateCellUIFromSolution(i, j, solutionBoard[i][j]);
                }
            }
            resetCellColors();
            loadBoardValuesFromUI();
            restartPause();
            restartVisibleStackPane();
            sudokuGrid.getStyleClass().remove("paused-border");
            time.stopTimer();
        }
    }
    //==================================================================================================================
    //==================================LẤY CÁC THÀNH PHẦN UI===========================================================
    public boolean areAllLabelsFilled(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                for (Node child : stackPane.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        String text = label.getText();
                        Color lbColor = (Color) label.getTextFill();
                        if (text == null || text.trim().isEmpty() || lbColor.equals(Color.RED)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    //Lấy label từ một stack pane
    private Label getLabelFromStackPane(StackPane cell) {
        for (Node child : cell.getChildren()) {
            if (child instanceof Label) {
                return (Label) child;
            }
        }
        return null;
    }
    //Lấy tọa độ một ô grid pane
    private Coordinates getGridCoordinates(Node node) {
        int row = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
        int col = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
        return new Coordinates(row, col);
    }
    //Lấy một stack pane bằng vị trí cụ thể
    private StackPane getStackPaneFromCoordinates(Coordinates coordinates) {
        int row = coordinates.getX();
        int col = coordinates.getY();
        for (Node node : sudokuGrid.getChildren()) {
            int nodeRow = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
            int nodeCol = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
            if (nodeRow == row && nodeCol == col && node instanceof StackPane) {
                return (StackPane) node;
            }
        }
        return null;
    }
    //Lấy grid pane được chứa trong stack pane
    private GridPane getNotesGridFromStackPane(StackPane stackPane) {
        if (stackPane == null) return null;
        for (Node child : stackPane.getChildren()) {
            if (child instanceof GridPane) {
                return (GridPane) child;
            }
        }
        return null;
    }
    //==================================================================================================================
}
