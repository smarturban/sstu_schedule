package com.ssu.schedule.repository;

import com.ssu.schedule.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findAllByFacultyId(String facultyId);
}
