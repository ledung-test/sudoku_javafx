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
        alert.setHeaderText(null);
        alert.setContentText("Chúc mừng! Bạn đã hoàn thành bảng Sudoku!");
        time.stopTimer();
        alert.showAndWait();
    }
    public boolean showAlertGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("Bạn đã nhập sai 3 lần. Hết lượt chơi!");
        alert.setContentText("Bạn muốn cơ hội lần 2 không?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public void showSuggestCount(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã hết lượt gợi ý");
        alert.showAndWait();
    }
    public boolean showConfirmationDifficulty() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Thay đổi chế độ chơi");
        confirmationDialog.setContentText("Bạn có chắc chắn muốn chọn chế độ này không?");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public boolean showConfirmationSolution() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Xác nhận");
        confirmationDialog.setHeaderText("Hiện thị lời giải");
        confirmationDialog.setContentText("Bạn có chắc chắn muốn hiện thị lời giải không?");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
