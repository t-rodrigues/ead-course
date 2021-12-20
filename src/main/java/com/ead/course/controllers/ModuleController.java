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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping
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

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        var moduleOptional = this.moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        this.moduleService.delete(moduleOptional.get());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleModel> updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId,
            @RequestBody @Valid ModuleDto moduleDto) {
        var moduleOptional = this.moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var module = moduleOptional.get();
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());

        this.moduleService.save(module);

        return ResponseEntity.ok(module);
    }

    @GetMapping
    public ResponseEntity<List<ModuleModel>> getModules(@PathVariable UUID courseId) {
        var modules = this.moduleService.findAllByCourse(courseId);

        return ResponseEntity.ok(modules);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleModel> getModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        var moduleOptional = this.moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var module = moduleOptional.get();

        return ResponseEntity.ok(module);
    }

}
