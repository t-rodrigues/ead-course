package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID>, JpaSpecificationExecutor<LessonModel> {

    @Query("SELECT l FROM LessonModel l WHERE l.module.moduleId = :moduleId")
    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

    @Query("SELECT l FROM LessonModel l WHERE l.lessonId = :lessonId AND l.module.moduleId = :moduleId")
    Optional<LessonModel> findLessonIntoModule(UUID lessonId, UUID moduleId);

}
