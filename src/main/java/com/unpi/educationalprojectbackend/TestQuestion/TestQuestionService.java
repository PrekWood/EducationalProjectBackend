package com.unpi.educationalprojectbackend.TestQuestion;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TestQuestionService {
    private final TestQuestionRepository repository;

    public TestQuestion save(TestQuestion object){
        return repository.save(object);
    }

    public TestQuestion find(Long id) throws ObjectNotFoundException {
        Optional<TestQuestion> testQuestion = repository.findById(id);
        if(testQuestion.isEmpty()){
            throw new ObjectNotFoundException(TestQuestion.class,"not found");
        }
        return testQuestion.get();
    }

    public void delete(TestQuestion object){
        repository.deleteFromTestAttempts(object.getId());
        repository.delete(object);
    }

    public boolean isAnswerCorrect(Long idQuestion, Long idAnswer){
        Long idCorrectAnswer = repository.getCorrectAnswerOfQuestion(idQuestion);
        return Objects.equals(idAnswer, idCorrectAnswer);
    }

    public List<TestAnswer> getTestAnswers(TestQuestion question){
        return repository.getTestAnswers(question.getId());
    }
}
