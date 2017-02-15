package com.ssu.schedule.parser;

import com.ssu.schedule.model.*;
import com.ssu.schedule.repository.FacultyRepository;
import com.ssu.schedule.service.LocalStorage;
import com.ssu.schedule.model.LessonTime;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class ParserSSU {
    private static final String UNIVERSITY = "ssu";

    @Value("${ssu.basicAuth.login}")
    private String login;

    @Value("${ssu.basicAuth.password}")
    private String password;

    @Value("${ssu.url}")
    private String url;

    private final FacultyRepository facultyRepository;
    private final LocalStorage storage;

    private Time time = new Time();

    @Autowired
    public ParserSSU(FacultyRepository facultyRepository, LocalStorage storage) {
        this.facultyRepository = facultyRepository;
        this.storage = storage;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        OkHttpClient client = new OkHttpClient();
        String credentials = Credentials.basic(login, password);

        Request.Builder requestBuilder = new Request.Builder()
                .addHeader("Authorization", credentials);

        try {
            // Get information about a faculties
            Request request = requestBuilder
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            parseNameOfFaculties(XML.toJSONObject(response.body().string()));

            // Get information about a groups for each faculty
            for (Faculty faculty : storage.getFaculties()) {
                request = requestBuilder
                        .url(url + "?dep=" + faculty.getId())
                        .build();

                response = client.newCall(request).execute();
                parseFaculties(faculty, XML.toJSONObject(response.body().string()));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseNameOfFaculties(JSONObject object) throws JSONException {
        JSONArray faculties = object.getJSONObject("departments").getJSONArray("department");
        ArrayList<Faculty> facultyStorage = new ArrayList<>();

        for (int i = 0; i < faculties.length(); i++) {
            JSONObject item = faculties.getJSONObject(i);

            Faculty faculty = new Faculty();
            faculty.setId(item.getString("id"));
            faculty.setName(item.getString("name"));
            faculty.setUniversity(UNIVERSITY);

            facultyStorage.add(faculty);
            facultyRepository.save(faculty);
        }

        storage.setFaculties(facultyStorage);
    }

    private void parseFaculties(Faculty faculty, JSONObject object) throws JSONException {
        JSONArray groupItems = object.getJSONObject("schedule").getJSONArray("group");

        ArrayList<Group> groups = new ArrayList<>();

        for (int i = 0; i < groupItems.length(); i++) {
            Group group = new Group();
            JSONObject groupItem = groupItems.getJSONObject(i);
            String number = groupItem.getString("number");

            group.setName(number);
            group.setId(UNIVERSITY + "_" + faculty.getId() + "_" + number);

            ArrayList<Day> days = parseDays(groupItem.getJSONArray("day"));
            group.setDays(days);
            groups.add(group);
        }

        faculty.setGroups(groups);
        storage.setFaculty(faculty);
    }

    private ArrayList<Day> parseDays(JSONArray jsonDays) throws JSONException {
        ArrayList<Day> days = new ArrayList<>();

        for (int i = 0; i < jsonDays.length(); i++) {
            JSONArray lessonArray = new JSONArray();
            JSONObject obj = jsonDays.getJSONObject(i);
            Day day = new Day();

            try {
                lessonArray = obj.getJSONObject("lessons").getJSONArray("lesson");
            } catch (JSONException e) {
                if (e.getMessage().contains("is not a JSONArray")) {
                    lessonArray.put(obj.getJSONObject("lessons").getJSONObject("lesson"));
                }
            }

            day.setId(obj.getString("id"));
            day.setLessons(parseLessons(lessonArray));
            days.add(day);
        }

        return days;
    }

    private ArrayList<Lesson> parseLessons(JSONArray jsonLessons) throws JSONException {
        ArrayList<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < jsonLessons.length(); i++) {
            Lesson lesson = new Lesson();
            Teacher teacher = new Teacher();
            Auditory auditory = new Auditory();

            JSONObject jsonLesson = jsonLessons.getJSONObject(i);

            lesson.setId(jsonLesson.getString("num"));
            lesson.setTitle(jsonLesson.getString("name"));

            switch (jsonLesson.getString("weektype")) {
                case "nom":
                    lesson.setParity(1);
                    break;
                case "denom":
                    lesson.setParity(2);
                    break;
            }

            switch (jsonLesson.getString("type")) {
                case "practice":
                    lesson.setType(0);
                    break;
            }

            try {
                LessonTime lessonTime = time.getLessonTime(Integer.parseInt(jsonLesson.getString("num")) - 1);
                lesson.setTimeStart(lessonTime.getStart());
                lesson.setTimeEnd(lessonTime.getEnd());
            } catch (Exception e) {
                // For >= 8 lessons. Bullshit...
            }

            JSONObject jsonTeacher = jsonLesson.getJSONObject("teacher");
            teacher.setFirstName(jsonTeacher.getString("name"));
            teacher.setMiddleName(jsonTeacher.getString("patronim"));
            teacher.setLastName(jsonTeacher.getString("lastname"));
            teacher.setFullName(jsonTeacher.getString("compiled_fio"));
            lesson.setTeachers(Collections.singletonList(teacher));

            auditory.setName(jsonLesson.getString("place"));
            lesson.setAuditories(Collections.singletonList(auditory));

            lessons.add(lesson);
        }

        return lessons;
    }
}
