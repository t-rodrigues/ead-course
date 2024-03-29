package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
        var course = new CourseModel();

        BeanUtils.copyProperties(courseDto, course);
        course.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        course.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        this.courseService.save(course);

        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(course.getCourseId()).toUri();

        return ResponseEntity.created(location).body(course);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseDto courseDto) {
        var courseOptional = this.courseService.findById(courseId);

        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var courseModel = courseOptional.get();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCourseId(courseId);
        courseModel.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        this.courseService.save(courseModel);

        return ResponseEntity.ok(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID courseId) {
        var course = this.courseService.findById(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        this.courseService.delete(course.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getCourses(SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) UUID userId) {
        Page<CourseModel> page = null;
        if (userId != null) {
            page = this.courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable);
        } else {
            page = this.courseService.findAll(spec, pageable);

        }

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseModel> getCourse(@PathVariable UUID courseId) {
        var course = this.courseService.findById(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(course.get());
    }

}
