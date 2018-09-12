package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Group {

    @JsonIgnore
    private String id;

    private String facultyId;

    private String name;

    private List<Lesson> lessons;

    public String getName() {
        return name;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
