package com.ead.course.services;

import com.ead.course.models.CourseModel;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseModel course);

    Optional<CourseModel> findById(UUID courseId);

}
