package com.ssu.schedule.repository;

import com.ssu.schedule.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    @Query("{ '_id' : ?0 }")
    List<Group> findAll(String id);
}
