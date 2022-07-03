package com.unpi.educationalprojectbackend.TestAnswer;

import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TestAnswerService {
    private final TestAnswerRepository repository;
    public TestAnswer save(TestAnswer object){
        return repository.save(object);
    }
    public void delete(TestAnswer object){
        repository.removeFromTestAttemptForeignKey(object.getId());
        repository.removeFromCorrectAnswerForeignKey(object.getId());
        repository.delete(object);
    }
    public TestAnswer find(Long idAnswer) throws ObjectNotFoundException{
        Optional<TestAnswer> answerFound = repository.findById(idAnswer);
        if(answerFound.isPresent()){
            return answerFound.get();
        }
        throw new ObjectNotFoundException(TestAnswer.class,"not found");
    }
}
