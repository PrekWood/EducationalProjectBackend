package com.unpi.educationalprojectbackend.TestQuestion;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.Chapter.ChapterService;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapterService;
import com.unpi.educationalprojectbackend.SubChapter.requests.SubChapterUpdateRequest;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswerService;
import com.unpi.educationalprojectbackend.TestAnswer.request.TestAnswerCreateRequest;
import com.unpi.educationalprojectbackend.TestQuestion.requests.TestQuestionCreateRequest;
import com.unpi.educationalprojectbackend.User.User;
import com.unpi.educationalprojectbackend.User.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TestQuestionController extends ResponseHandler {

    public UserService userService;
    public ChapterService chapterService;
    public TestAnswerService testAnswerService;
    public TestQuestionService testQuestionService;

    @PostMapping("chapter/{idChapter}/question")
    public ResponseEntity<?> create(
            @PathVariable Long idChapter,
            @RequestBody TestQuestionCreateRequest requestBody
    ) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        // Search for chapter
        Chapter chapter = null;
        try {
            chapter = chapterService.find(idChapter);
        } catch (ObjectNotFoundException e) {
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }

        // Create Question
        TestQuestion question = new TestQuestion();
        question.setChapter(chapter);
        question.setQuestion(requestBody.getQuestion());
        question.setType(requestBody.getType());
        question.setDifficulty(requestBody.getDifficulty());
        question.setErrorType(requestBody.getErrorType());
        testQuestionService.save(question);

        // Create Answers
        TestAnswer correctAnswer = null;
        for (TestAnswerCreateRequest requestAnswer : requestBody.getAnswers()) {
            TestAnswer answer = new TestAnswer();
            answer.setQuestion(question);
            answer.setAnswer(requestAnswer.getAnswer());
            testAnswerService.save(answer);

            if(requestAnswer.isCorrect()){
                correctAnswer = answer;
            }
        }

        question.setCorrectAnswer(correctAnswer);
        testQuestionService.save(question);

        return createSuccessResponse(HttpStatus.CREATED);
    }


    @DeleteMapping("question/{idQuestion}")
    public ResponseEntity<?> delete(@PathVariable Long idQuestion) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        // Search for chapter
        TestQuestion question = null;
        try {
            question = testQuestionService.find(idQuestion);
        } catch (ObjectNotFoundException e) {
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }

        List<TestAnswer> testAnswerList = testQuestionService.getTestAnswers(question);
        for (TestAnswer testAnswer : testAnswerList) {
            testAnswerService.delete(testAnswer);
        }
        testQuestionService.delete(question);

        return createSuccessResponse();
    }


    @GetMapping("question/{idQuestion}")
    public ResponseEntity<?> getDetails(@PathVariable Long idQuestion) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        // Search for chapter
        TestQuestion question = null;
        try {
            question = testQuestionService.find(idQuestion);
        } catch (ObjectNotFoundException e) {
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }

        List<TestAnswer> testAnswerList = testQuestionService.getTestAnswers(question);
        for (TestAnswer testAnswer : testAnswerList) {
            testAnswer.setQuestion(null);
        }
        question.setAnswerList(testAnswerList);

        return createSuccessResponse(question);
    }

    @PutMapping("question/{idQuestion}")
    public ResponseEntity<?> update(@PathVariable Long idQuestion, @RequestBody TestQuestionCreateRequest requestBody) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        // Search for chapter
        TestQuestion question = null;
        try {
            question = testQuestionService.find(idQuestion);
        } catch (ObjectNotFoundException e) {
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }

        // Update Question
        question.setQuestion(requestBody.getQuestion());
        question.setType(requestBody.getType());
        question.setDifficulty(requestBody.getDifficulty());
        question.setErrorType(requestBody.getErrorType());
        testQuestionService.save(question);

        // Delete old Answers
        List<TestAnswer> testAnswerList = testQuestionService.getTestAnswers(question);
        for (TestAnswer testAnswer : testAnswerList) {
            testAnswerService.delete(testAnswer);
        }

        // Create Answers
        TestAnswer correctAnswer = null;
        for (TestAnswerCreateRequest requestAnswer : requestBody.getAnswers()) {
            TestAnswer answer = new TestAnswer();
            answer.setQuestion(question);
            answer.setAnswer(requestAnswer.getAnswer());
            testAnswerService.save(answer);

            if(requestAnswer.isCorrect()){
                correctAnswer = answer;
            }
        }

        question.setCorrectAnswer(correctAnswer);
        testQuestionService.save(question);

        return createSuccessResponse();
    }
}
