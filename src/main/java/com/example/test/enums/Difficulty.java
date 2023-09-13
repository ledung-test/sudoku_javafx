package com.example.test.enums;

public enum Difficulty {
    EASY(22),
    MEDIUM(25),
    HARD(27);

    private final int removals;

    Difficulty(int removals) {
        this.removals = removals;
    }

    public int getRemovals() {
        return removals;
    }

}
