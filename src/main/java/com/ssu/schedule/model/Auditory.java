package com.ssu.schedule.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auditory")
public class Auditory {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
