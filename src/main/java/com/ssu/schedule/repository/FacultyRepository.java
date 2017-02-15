package com.ssu.schedule.repository;

import com.ssu.schedule.model.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FacultyRepository extends MongoRepository<Faculty, String> {
    List<Faculty> findById(String id);
    List<Faculty> findByName(String name);
    List<Faculty> findByUniversity(String university);
}
