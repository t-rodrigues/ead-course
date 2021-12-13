package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query("SELECT l FROM LessonModel l WHERE l.modules.moduleId = :moduleId")
    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

}
