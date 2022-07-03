package com.unpi.educationalprojectbackend.Test;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.Chapter.ChapterService;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.Test.requests.TestAnswerRequest;
import com.unpi.educationalprojectbackend.Test.requests.TestSubmitRequest;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswerService;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttempt;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttemptService;
import com.unpi.educationalprojectbackend.TestAttemptAnswer.TestAttemptAnswer;
import com.unpi.educationalprojectbackend.TestAttemptAnswer.TestAttemptAnswerService;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestionService;
import com.unpi.educationalprojectbackend.User.User;
import com.unpi.educationalprojectbackend.User.UserService;
import com.unpi.educationalprojectbackend.UserProgress.UserProgressService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TestController extends ResponseHandler {
    public ChapterService chapterService;
    public TestQuestionService testQuestionService;
    public UserService userService;
    public TestAttemptService testAttemptService;
    public TestAttemptAnswerService testAttemptAnswerService;
    public TestAnswerService testAnswerService;
    public UserProgressService userProgressService;

    @GetMapping("test/{idChapter}/")
    public ResponseEntity<?> getTestDetails(@PathVariable Long idChapter) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        HashMap<String, Object> responseBody = new HashMap<>();


        // Search for chapter
        Chapter chapter = null;
        try{
            chapter = chapterService.find(idChapter);
        }catch (ObjectNotFoundException e){
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }
        responseBody.put("chapter", chapterService.present(chapter));

        // Get All questions
        List<TestQuestion> questionList = chapterService.getTestQuestions(chapter.getId());
        for (TestQuestion testQuestion:questionList) {
            List<TestAnswer> testAnswerList = testQuestionService.getTestAnswers(testQuestion);
            for (TestAnswer testAnswer:testAnswerList) {
                testAnswer.setQuestion(null);
            }
            testQuestion.setChapter(null);
            testQuestion.setAnswerList(testAnswerList);
        }

        // Shuffle
        Collections.shuffle(questionList);

        // Send only 70% of the answers
        List<TestQuestion> partialQuestionsList = questionList.subList(0,(int)Math.ceil(questionList.size()*0.7));
        responseBody.put("questions", partialQuestionsList);


        // Get Previews Tries
        List<Object> previousAttempts = userProgressService.getPreviousTestAttempts(
                chapter.getId(),
                loggedInUser.getProgress().getId()
        );
        responseBody.put("previousAttempts", previousAttempts);

        return createSuccessResponse(responseBody);
    }

    @PostMapping("test/{idChapter}/")
    public ResponseEntity<?> submitTest(@PathVariable Long idChapter, @RequestBody TestSubmitRequest request) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        // Search for chapter
        Chapter chapter = null;
        try{
            chapter = chapterService.find(idChapter);
        }catch (ObjectNotFoundException e){
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }

        // Initiate attempt
        TestAttempt testAttempt = new TestAttempt();
        testAttempt.setUser(loggedInUser);
        testAttempt.setChapter(chapter);
        testAttemptService.save(testAttempt);

        List<TestAttemptAnswer> answersList = new ArrayList<>();
        for (TestAnswerRequest answerReceived : request.getAnswers()) {
            // Search for answer and question by id
            TestAnswer answer = null;
            TestQuestion question = null;
            try{
                answer = testAnswerService.find(answerReceived.getIdAnswer());
                question = testQuestionService.find(answerReceived.getIdQuestion());
            }catch (Exception e){
                return createErrorResponse("Κάτι πήγε στραβα");
            }

            // Create attempt answer
            TestAttemptAnswer testAttemptAnswer = new TestAttemptAnswer();
            testAttemptAnswer.setAnswer(answer);
            testAttemptAnswer.setQuestion(question);
            testAttemptAnswer.setAttempt(testAttempt);
            testAttemptAnswer.setCorrect(testQuestionService.isAnswerCorrect(
                question.getId(),
                answer.getId()
            ));
            testAttemptAnswerService.save(testAttemptAnswer);

            answersList.add(testAttemptAnswer);
        }

        // Get correct answers count
        int correctAnswersCount = 0;
        for (TestAttemptAnswer answer:answersList) {
            if(answer.isCorrect()){
                correctAnswersCount += 1;
            }
        }

        // Get Results
        Float percentage = (float) correctAnswersCount / (float) answersList.size();
        GRADES grade = null;
        if(percentage >= 0.75){
            grade = GRADES.THREE;
        } else if (percentage >= 0.5) {
            grade = GRADES.TWO;
        } else {
            grade = GRADES.ONE;
        }

        // Update Db
        testAttempt.setAnswers(answersList);
        testAttempt.setPercentage(percentage);
        testAttempt.setGrade(grade);
        testAttemptService.save(testAttempt);

        for (TestAttemptAnswer answer:answersList) {
            TestQuestion question = answer.getQuestion();
            question.setAnswerList(testQuestionService.getTestAnswers(question));
            for (TestAnswer questionAnswer:question.getAnswerList()) {
                questionAnswer.setQuestion(null);
            }
        }
        testAttempt.setAnswers(answersList);

        return createSuccessResponse(testAttemptService.present(testAttempt));
    }


}
