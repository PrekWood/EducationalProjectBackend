package com.unpi.educationalprojectbackend.TestQuestion.requests;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAnswer.request.TestAnswerCreateRequest;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionDifficulty;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionErrorType;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestQuestionCreateRequest {
    private String question;
    private List<TestAnswerCreateRequest> answers;
    private QuestionType type;
    private QuestionDifficulty difficulty;
    private QuestionErrorType errorType;
}
