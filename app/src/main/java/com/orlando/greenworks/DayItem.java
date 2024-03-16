package com.orlando.greenworks;

public class DayItem {
    private final int day;
    private final int month;
    private final int year;
    private int frequency = 1;

    public DayItem(int day, int month, int year, int frequency) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.frequency = frequency;
    }

    public DayItem(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
