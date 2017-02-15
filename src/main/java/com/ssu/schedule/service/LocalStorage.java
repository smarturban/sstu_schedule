package com.ssu.schedule.service;

import com.ssu.schedule.model.Faculty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
public class LocalStorage {
    HashMap<String, Faculty> faculties = new HashMap<>();

    public LocalStorage() {
    }

    public void setFaculties(ArrayList<Faculty> faculties) {
        faculties.forEach(this::setFaculty);
    }

    public void setFaculty(Faculty faculty) {
        this.faculties.put(faculty.getUniversity() + "_" + faculty.getId(), faculty);
    }

    public Collection<Faculty> getFaculties() {
        return faculties.values();
    }

    public Faculty getFacultyById(String id) {
        return faculties.get(id);
    }
}
