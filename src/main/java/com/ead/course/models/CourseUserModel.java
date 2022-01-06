package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@Table(name = "tb_courses_users")
public class CourseUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CourseModel course;

    @Column(nullable = false)
    private UUID userId;

}
