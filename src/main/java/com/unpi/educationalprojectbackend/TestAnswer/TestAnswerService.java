package com.unpi.educationalprojectbackend.TestAnswer;

import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestAnswerService {
    private final TestAnswerRepository repository;
    public TestAnswer save(TestAnswer object){
        return repository.save(object);
    }
    public void delete(TestAnswer object){
        repository.delete(object);
    }
}
