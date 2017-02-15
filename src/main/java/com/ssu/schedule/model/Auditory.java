package com.ssu.schedule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ssu.schedule.jsonview.View;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auditory")
public class Auditory {
    @JsonView(View.SCHEDULE.class)
    @JsonProperty("auditory_name")
    private String name;

//    @JsonView(View.SCHEDULE.class)
    @JsonProperty("auditory_address")
    private String address;

    public Auditory() {
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
