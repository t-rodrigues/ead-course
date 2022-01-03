package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RestController
@RequestMapping("/modules/{moduleId}/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    public LessonController(LessonService lessonService, ModuleService moduleService) {
        this.lessonService = lessonService;
        this.moduleService = moduleService;
    }

    @PostMapping
    public ResponseEntity<Object> saveLesson(@PathVariable UUID moduleId, @RequestBody @Valid LessonDto lessonDto) {
        var moduleModelOptional = this.moduleService.findById(moduleId);

        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());
        this.lessonService.save(lessonModel);

        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{moduleId}")
                .buildAndExpand(lessonModel.getLessonId()).toUri();

        return ResponseEntity.created(location).body(lessonModel);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        var lessonOptional = this.lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        this.lessonService.delete(lessonOptional.get());

        return ResponseEntity.noContent().build();
    }

}
