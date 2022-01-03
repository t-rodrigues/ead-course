package com.ead.course.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDto {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String videoUrl;

}
