package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public LessonModel save(LessonModel lessonModel) {
        return this.lessonRepository.save(lessonModel);
    }

    @Override
    public Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId) {
        return this.lessonRepository.findLessonIntoModule(lessonId, moduleId);
    }

    @Override
    public void delete(LessonModel lessonModel) {
        this.lessonRepository.delete(lessonModel);
    }

}
