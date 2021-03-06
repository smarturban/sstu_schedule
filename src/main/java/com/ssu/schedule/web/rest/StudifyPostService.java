package com.ssu.schedule.web.rest;

import com.google.gson.Gson;
import com.ssu.schedule.model.University;
import com.ssu.schedule.parser.SsuScheduleParserService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class StudifyPostService {
    private static final Logger log = LoggerFactory.getLogger(StudifyPostService.class);

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

    @Value("${import.scheduleDir}")
    private String scheduleDir;

    private final OkHttpClient httpClient;

    private final Gson gson;

    private final SsuScheduleParserService ssuScheduleParserService;

    @Autowired
    public StudifyPostService(OkHttpClient httpClient, Gson gson, SsuScheduleParserService ssuScheduleParserService) {
        this.httpClient = httpClient;
        this.gson = gson;
        this.ssuScheduleParserService = ssuScheduleParserService;
    }

    @PostConstruct
    private void postScheduleToService() {
        try {
            University currentSchedule = ssuScheduleParserService.getSchedule();
            String schedule = gson.toJson(currentSchedule);

            saveToFile(schedule);

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

            log.info("Publish schedule started. Content length: " + schedule.length());

            Response response = httpClient.newCall(request).execute();

            log.info(response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(String schedule) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String fileName = now.format(DateTimeFormatter.ISO_DATE_TIME);
        String filePath = String.format("%s/schedule-%s.json", scheduleDir, fileName);

        Files.createFile(Paths.get(filePath));
        Files.write(Paths.get(filePath), schedule.getBytes());
    }
}
