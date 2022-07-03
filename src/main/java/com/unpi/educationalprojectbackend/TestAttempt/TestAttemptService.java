package com.unpi.educationalprojectbackend.TestAttempt;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAttemptAnswer.TestAttemptAnswer;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Service
public class TestAttemptService {
    private final TestAttemptRepository repository;

    public TestAttempt save(TestAttempt object){
        return repository.save(object);
    }

    public List<TestAttemptAnswer> loadAnswers(TestAttempt object){
        return repository.getAnswers(object.getId());
    }

    public Object present(TestAttempt attempt){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        HashMap<String, Object> presentedAttempt = new HashMap<>();
        presentedAttempt.put("id",attempt.getId());
        presentedAttempt.put("dateAdd",formatter.format(attempt.getDateAdd()));
//        presentedAttempt.put("chapter",attempt.getChapter());
        presentedAttempt.put("user",null);
        presentedAttempt.put("grade",attempt.getGrade());
        presentedAttempt.put("percentage",attempt.getPercentage());

        // We have to null certain fields so that we do not get a loop
        if(attempt.getAnswers() != null){
            for (TestAttemptAnswer answer:attempt.getAnswers()) {
                answer.setAttempt(null);

                TestQuestion question = answer.getQuestion();
                if(question != null){
                    TestAnswer correctAnswer = question.getCorrectAnswer();
                    correctAnswer.setQuestion(null);
                    question.setCorrectAnswer(correctAnswer);
                    answer.setQuestion(question);
                }
            }
            presentedAttempt.put("answers",attempt.getAnswers());
        }

        return presentedAttempt;
    }

    public List<Object> present(List<TestAttempt> attemptList){

        List<Object> presentedList = new ArrayList<>();
        for (TestAttempt attempt:attemptList) {
            presentedList.add(present(attempt));
        }
        return presentedList;
    }
}
