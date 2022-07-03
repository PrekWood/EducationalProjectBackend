package com.unpi.educationalprojectbackend.TestAttemptAnswer;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TestAttemptAnswerService {
    private final TestAttemptAnswerRepository repository;

    public TestAttemptAnswer save(TestAttemptAnswer object){
        return repository.save(object);
    }
}
