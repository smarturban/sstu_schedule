package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ssu.schedule.jsonview.View;

import java.util.List;

public class Lesson {
    @JsonIgnore
    @JsonView(View.SCHEDULE.class)
    private String id;

    @JsonView(View.SCHEDULE.class)
    private int type = 2;

    @JsonView(View.SCHEDULE.class)
    private int parity = 0;

    @JsonView(View.SCHEDULE.class)
    @JsonProperty("subject")
    private String title;

    @JsonView(View.SCHEDULE.class)
    @JsonProperty("time_start")
    private String timeStart;

    @JsonView(View.SCHEDULE.class)
    @JsonProperty("time_end")
    private String timeEnd;

    @JsonView(View.SCHEDULE.class)
    private List<Teacher> teachers;

    @JsonView(View.SCHEDULE.class)
    private List<Auditory> auditories;

    public void setType(int type) {
        this.type = type;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimeStart(String time_start) {
        this.timeStart = time_start;
    }

    public void setTimeEnd(String time_end) {
        this.timeEnd = time_end;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Auditory> getAuditories() {
        return auditories;
    }

    public void setAuditories(List<Auditory> auditories) {
        this.auditories = auditories;
    }

    public int getType() {
        return type;
    }

    public int getParity() {
        return parity;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }
}
