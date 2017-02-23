package com.ssu.schedule.parser;

import com.ssu.schedule.model.*;
import com.ssu.schedule.repository.FacultyRepository;
import com.ssu.schedule.repository.GroupRepository;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ParserSSU {

    private static final Logger log = LoggerFactory.getLogger(ParserSSU.class);

    @Value("${ssu.basicAuth.login}")
    private String login;

    @Value("${ssu.basicAuth.password}")
    private String password;

    @Value("${ssu.url}")
    private String url;

    @Value("${ssu.name}")
    private String univerName;

    @Value("${ssu.abbr}")
    private String univerAbbr;

    private final FacultyRepository facultyRepository;
    private final GroupRepository groupRepository;

    private TimeList timeList = new TimeList();

    @Autowired
    public ParserSSU(FacultyRepository facultyRepository, GroupRepository groupRepository) {
        this.facultyRepository = facultyRepository;
        this.groupRepository = groupRepository;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void getCurrentSchedule() {
        OkHttpClient client = new OkHttpClient();
        String credentials = Credentials.basic(login, password);

        Request.Builder requestBuilder = new Request.Builder()
                .addHeader("Authorization", credentials);

        try {
            log.debug("Update schedule started");

            // Get information about a faculties
            Request request = requestBuilder
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            List<Faculty> faculties = parseNameOfFaculties(XML.toJSONObject(response.body().string()));

            // Get information about a groups for each faculty
            for (Faculty faculty : faculties) {
                request = requestBuilder
                        .url(url + "?dep=" + faculty.getId())
                        .build();

                response = client.newCall(request).execute();
                parseFaculty(faculty, XML.toJSONObject(response.body().string()));
            }

            log.debug("Update schedule successfully finished");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Faculty> parseNameOfFaculties(JSONObject object) throws JSONException {
        JSONArray faculties = object.getJSONObject("departments").getJSONArray("department");
        List<Faculty> facultyStorage = new ArrayList<>();

        for (int i = 0; i < faculties.length(); i++) {
            JSONObject item = faculties.getJSONObject(i);

            Faculty faculty = new Faculty();
            faculty.setId(item.getString("id"));
            faculty.setName(item.getString("name"));

            facultyStorage.add(faculty);
            facultyRepository.save(faculty);
        }

        return facultyStorage;
    }

    private void parseFaculty(Faculty faculty, JSONObject object) throws JSONException {
        JSONArray groupItems = object.getJSONObject("schedule").getJSONArray("group");
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < groupItems.length(); i++) {
            Group group = new Group();
            JSONObject groupItem = groupItems.getJSONObject(i);
            String number = groupItem.getString("number");

            group.setName(number);
            group.setFacultyId(faculty.getId());

            List<Lesson> lessons = parseDays(groupItem.getJSONArray("day"));
            group.setLessons(lessons);
            groups.add(group);
        }

        groupRepository.save(groups);
    }


    private List<Lesson> parseDays(JSONArray jsonDays) throws JSONException {
        List<Lesson> lessonsPerDay = new ArrayList<>();

        for (int i = 0; i < jsonDays.length(); i++) {
            JSONArray lessonArray = new JSONArray();
            JSONObject obj = jsonDays.getJSONObject(i);

            try {
                lessonArray = obj.getJSONObject("lessons").getJSONArray("lesson");
            } catch (JSONException e) {
                if (e.getMessage().contains("is not a JSONArray")) {
                    lessonArray.put(obj.getJSONObject("lessons").getJSONObject("lesson"));
                }
            }

            lessonsPerDay.addAll(parseLessons(obj.getString("id"), lessonArray));
        }

        return lessonsPerDay;
    }

    private ArrayList<Lesson> parseLessons(String id, JSONArray jsonLessons) throws JSONException {
        ArrayList<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < jsonLessons.length(); i++) {
            Day day = new Day();
            Lesson lesson = new Lesson();
            Teacher teacher = new Teacher();
            Auditory auditory = new Auditory();

            JSONObject jsonLesson = jsonLessons.getJSONObject(i);

            lesson.setId(jsonLesson.getString("num"));
            lesson.setSubject(jsonLesson.getString("name"));

            switch (jsonLesson.getString("weektype")) {
                case "nom":
                    day.setWeek(2);
                    break;
                case "denom":
                    day.setWeek(1);
                    break;
            }

            day.setWeekday(Integer.parseInt(id));
            lesson.setDate(day);

            switch (jsonLesson.getString("type")) {
                case "practice":
                    lesson.setType("Практика");
                    break;
            }

            int index = Integer.parseInt(jsonLesson.getString("num")) - 1;
            Time time = new Time();

            if (index < timeList.size()) {
                LessonTime lessonTime = timeList.getLessonTime(index);
                time.setStart(lessonTime.getStart());
                time.setEnd(lessonTime.getEnd());
            }

            lesson.setTime(time);

            JSONObject jsonTeacher = jsonLesson.getJSONObject("teacher");
            teacher.setName(jsonTeacher.getString("compiled_fio"));
            lesson.setTeachers(Collections.singletonList(teacher));

            auditory.setName(jsonLesson.getString("place"));
            lesson.setAudiences(Collections.singletonList(auditory));

            lessons.add(lesson);
        }

        return lessons;
    }
}
