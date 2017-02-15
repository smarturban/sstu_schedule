package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "faculties")
@JsonIgnoreProperties({"university", "groups"})
public class Faculty {
    @Id
    @JsonProperty("faculty_id")
    private String id;

    @JsonProperty("faculty_name")
    private String name;

    private String university;

    private Map<String, Group> groups = new HashMap<>();

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUniversity() {
        return university;
    }

    public Collection<Group> getGroups() {
        return groups.values();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setGroups(ArrayList<Group> groups) {
        for (Group group : groups) {
            this.groups.put(group.getId(), group);
        }
    }

    public Group getGroup(String id) {
        return groups.get(id);
    }
}
