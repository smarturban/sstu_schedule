package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ssu.schedule.jsonview.View;

import java.util.ArrayList;

public class Group {
    @JsonView({View.GROUP.class})
    @JsonProperty("group_id")
    private String id;

    @JsonView({View.SCHEDULE.class, View.GROUP.class})
    @JsonProperty("group_name")
    private String name;

    @JsonView(View.SCHEDULE.class)
    private ArrayList<Day> days = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }
}
