package com.example.test.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Time {
    private Timeline timer;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty();
    private StringProperty timeString = new SimpleStringProperty();

    public void initTimer() {
        timeSeconds.set(0);
        timeString.set("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeSeconds.set(timeSeconds.get() + 1);
            int mins = timeSeconds.get() / 60;
            int secs = timeSeconds.get() % 60;
            timeString.set(String.format("%02d:%02d", mins, secs));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }
    public void printTimer(Label time_label){
        startTimer();
        time_label.textProperty().bind(timeString);
    }
    public void restartTimer(){
        stopTimer();
        timeSeconds.set(0);
        timeString.set("00:00");
        startTimer();
    }
    public void startTimer() {
        if (timer != null) {
            timer.play();
        }
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.pause();
        }
    }
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
