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
}
