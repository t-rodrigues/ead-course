package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository courseRepository, ModuleRepository moduleRepository,
            LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        var modules = this.moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if (!modules.isEmpty()) {
            modules.stream().forEach(module -> {
                var lessons = this.lessonRepository.findAllLessonsIntoModule(module.getModuleId());

                if (!lessons.isEmpty()) {
                    this.lessonRepository.deleteAll(lessons);
                }
            });
            this.moduleRepository.deleteAll(modules);
        }

        this.courseRepository.deleteById(courseModel.getCourseId());
    }

}
