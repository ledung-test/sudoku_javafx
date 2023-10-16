package com.example.test.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.util.Optional;

public class Message {
    private SudokuController sudokuController;
    private Time time;
    public Message(SudokuController sudokuController, Time time){
        this.sudokuController = sudokuController;
        this.time = time;
    }
    public void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText("Chúc mừng! Bạn đã hoàn thành bảng Sudoku!");
        time.stopTimer();
        alert.showAndWait();
    }
    public boolean showAlertGameOver() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Bạn đã nhập sai 3 lần");
        confirmationDialog.setHeaderText("Bạn muốn cơ hội lần 2 không?");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public void showSuggestCount(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Bạn đã hết lượt gợi ý");
        time.pauseTimer();
        alert.showAndWait();
    }
    public boolean showConfirmationDifficulty() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Thay đổi chế độ chơi");
        confirmationDialog.setHeaderText("Bắt đầu trò chơi mới?");
        time.pauseTimer();
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public boolean showConfirmationSolution() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Hiện thị lời giải");
        confirmationDialog.setHeaderText("Bạn có chắc chắn muốn hiện thị lời giải");
        time.pauseTimer();
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public static void showAlertGeneratorFailed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Bảng dưới đây có thể có nhiều cách giải.");
        alert.showAndWait();
    }
    public void showRule() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Luật chơi");
        alert.setHeaderText("- Sudoku là câu đố trí tuệ có hình dạng lưới gồm 9x9 ô trống.\n" +
                            "- Bạn chỉ có thể sử dụng các số từ 1 đến 9.\n" +
                            "- Mỗi khối gồm 3×3 ô trống chỉ có thể chứa các số từ 1 đến 9.\n" +
                            "- Mỗi hàng dọc chỉ có thể chứa các số từ 1 đến 9.\n" +
                            "- Mỗi hàng ngang chỉ có thể chứa các số từ 1 đến 9.\n" +
                            "- Mỗi số trong khối 3×3, hàng dọc hoặc hàng ngang không được trùng nhau.\n" +
                            "- Câu đố được giải khi điền đúng hết tất cả các số trên toàn bộ lưới Sudoku.");
        time.pauseTimer();
        alert.showAndWait();
    }
}
