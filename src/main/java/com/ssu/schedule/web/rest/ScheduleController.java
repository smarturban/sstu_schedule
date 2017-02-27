package com.ssu.schedule.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssu.schedule.model.Faculty;
import com.ssu.schedule.model.Group;
import com.ssu.schedule.model.University;
import com.ssu.schedule.repository.FacultyRepository;
import com.ssu.schedule.repository.GroupRepository;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Value("${university.name}")
    private String univerName;

    @Value("${university.abbr}")
    private String univerAbbr;

    @Value("${import.url}")
    private String importUrl;

    @Value("${import.token}")
    private String accessToken;

    @Value("${import.report}")
    private String reportEmail;

    @Value("${import.type}")
    private String importType;

    private final OkHttpClient client;
    private final Gson gson;

    @Autowired
    public ScheduleController() {
        this.client = new OkHttpClient();
        this.gson = new GsonBuilder().create();
    }

    @RequestMapping("/")
    public University getUniversity() {
        return getLastSchedule();
    }

    @Scheduled(cron = "0 20 12 1/1 * ?")
    public void postScheduleToService() {
        try {

            String schedule = gson.toJson(getLastSchedule());

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", accessToken)
                    .addFormDataPart("report", reportEmail)
                    .addFormDataPart("type", importType)
                    .addFormDataPart("datafile", "schedule.json",
                            RequestBody.create(MediaType.parse("application/json"), schedule))
                    .build();

            Request request = new Request.Builder()
                    .url(importUrl)
                    .post(requestBody)
                    .build();

            log.debug("Publish schedule started. Content length: " + schedule.length());
            Response response = client.newCall(request).execute();
            log.debug(response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private University getLastSchedule() {
        University university = new University();
        university.setName(univerName);
        university.setAbbr(univerAbbr);

        List<Faculty> faculties = facultyRepository.findAll();

        for (Faculty faculty : faculties) {
            List<Group> groups = groupRepository.findAllByFacultyId(faculty.getId());
            faculty.setGroups(groups);
        }

        university.setFaculties(faculties);

        return university;
    }
}
