package com.ssu.schedule.model;

import java.util.ArrayList;

public class Time {
    public static final ArrayList<LessonTime> schedule = new ArrayList<>();

    public Time() {
        schedule.add(new LessonTime("08:20", "09:50"));
        schedule.add(new LessonTime("10:00", "11:35"));
        schedule.add(new LessonTime("12:05", "13:40"));
        schedule.add(new LessonTime("13:50", "15:25"));
        schedule.add(new LessonTime("15:35", "17:10"));
        schedule.add(new LessonTime("17:20", "18:40"));
        schedule.add(new LessonTime("18:45", "20:05"));
    }

    public LessonTime getLessonTime(int index) {
        return schedule.get(index);
    }
}
