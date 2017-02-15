package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ssu.schedule.jsonview.View;

import java.util.List;

public class Day {
    @JsonView(View.SCHEDULE.class)
    @JsonProperty("weekday")
    private String id;

    @JsonView(View.SCHEDULE.class)
    private List<Lesson> lessons;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}
