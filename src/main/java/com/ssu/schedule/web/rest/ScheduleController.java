package com.ssu.schedule.web.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.ssu.schedule.jsonview.View;
import com.ssu.schedule.model.Faculty;
import com.ssu.schedule.model.Group;
import com.ssu.schedule.service.LocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
public class ScheduleController {
    @Autowired
    private LocalStorage storage;

    @RequestMapping("/get_faculties")
    public Map<String, Collection<Faculty>> getFaculties() {
        return Collections.singletonMap("faculties", storage.getFaculties());
    }

    @JsonView(View.GROUP.class)
    @RequestMapping("/get_groups")
    public Map<String, Collection<Group>> getGroups(@RequestParam("faculty_id") String facultyId) {
        Faculty faculty = storage.getFacultyById(facultyId);
        return Collections.singletonMap("groups", faculty.getGroups());
    }

    @JsonView(View.SCHEDULE.class)
    @RequestMapping("/get_schedule")
    public Group getSchedule(@RequestParam("group_id") String groupId) {
        String facultyId = groupId.substring(0, groupId.length() - 4);
        Faculty faculty = storage.getFacultyById(facultyId);
        return faculty.getGroup(groupId);
    }
}
