package com.unpi.educationalprojectbackend.ChapterGrade;

import com.unpi.educationalprojectbackend.UserProgress.UserProgress;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChapterGradeService {
    private final ChapterGradeRepository repository;
    public ChapterGrade save(ChapterGrade obj) {
        return repository.save(obj);
    }
}
