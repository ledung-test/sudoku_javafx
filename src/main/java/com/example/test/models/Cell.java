package com.example.test.models;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private int value;
    private boolean isEditable; // Xác định cell có thể chỉnh sửa ở thời điểm hiện tại hay không.
    private Set<Integer> notes;

    public Cell() {
        this.value = 0;
        this.isEditable = true;
        this.notes = new HashSet<>();
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.isEditable = value == 0;
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
