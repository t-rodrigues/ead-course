package com.ead.course.services.impl;

import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository, LessonRepository lessonRepository) {
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        var lessons = this.lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

        if (!lessons.isEmpty()) {
            this.lessonRepository.deleteAll(lessons);
        }

        this.moduleRepository.deleteById(moduleModel.getModuleId());
    }

}
