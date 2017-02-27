package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Lesson {
    @JsonIgnore
    private String id;

    private String type = "Лекция";

    private String subject = "Название предмета";

    private Time time;

    @JsonProperty("date")
    private Day date;

    private List<Teacher> teachers;

    private List<Auditory> audiences;

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Day date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Auditory> getAudiences() {
        return audiences;
    }

    public void setAudiences(List<Auditory> audiences) {
        this.audiences = audiences;
    }

    public String getType() {
        return type;
    }

    public Day getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
