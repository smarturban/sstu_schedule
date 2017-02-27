package com.ssu.schedule.model;

public class LessonTime {
    private String start;
    private String end;

    public LessonTime(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
