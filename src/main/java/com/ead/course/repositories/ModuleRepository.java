package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    @Query("SELECT m FROM ModuleModel m WHERE m.course.courseId = :courseId")
    List<ModuleModel> findAllModulesIntoCourse(UUID courseId);

    @Query("SELECT m FROM ModuleModel m WHERE m.course.courseId = :courseId AND m.moduleId = :moduleId")
    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

}
