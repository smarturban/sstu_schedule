package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.List;

@Document(collection = "faculties")
public class Faculty {
    @Id
    @JsonIgnore
    private String id;

    private String name;

    private List<Group> groups;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
