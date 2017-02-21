package com.ssu.schedule.model;

public class Day {
    private String start = "01.09.2016";
    private String end = "01.07.2017";
    private int weekday;

    private int week = 0;

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeekday() {
        return weekday;
    }

    public int getWeek() {
        return week;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

