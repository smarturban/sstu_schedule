package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class University {
    @JsonProperty("name")
    private String name;

    @JsonProperty("abbr")
    private String abbr;

    @JsonProperty("faculties")
    private List<Faculty> faculties = new ArrayList<>();

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}
