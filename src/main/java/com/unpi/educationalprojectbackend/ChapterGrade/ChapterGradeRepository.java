package com.unpi.educationalprojectbackend.ChapterGrade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterGradeRepository extends JpaRepository<ChapterGrade, Long> {

}
