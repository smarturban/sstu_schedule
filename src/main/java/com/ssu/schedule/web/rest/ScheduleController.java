package com.ssu.schedule.web.rest;

import com.ssu.schedule.model.Faculty;
import com.ssu.schedule.model.Group;
import com.ssu.schedule.model.University;
import com.ssu.schedule.repository.FacultyRepository;
import com.ssu.schedule.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScheduleController {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Value("${ssu.name}")
    private String univerName;

    @Value("${ssu.abbr}")
    private String univerAbbr;

    @RequestMapping("/")
    public University getUniversity() {
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
