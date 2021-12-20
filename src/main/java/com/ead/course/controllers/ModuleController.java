package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable UUID courseId, @RequestBody @Valid ModuleDto moduleDto) {
        var courseOptional = this.courseService.findById(courseId);

        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseOptional.get());
        this.moduleService.save(moduleModel);

        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{moduleId}")
                .buildAndExpand(moduleModel.getModuleId()).toUri();

        return ResponseEntity.created(location).body(moduleModel);
    }

}
