package com.unpi.educationalprojectbackend.Test;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.Chapter.ChapterService;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGradeService;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.TYPE;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.SubChapter.SubChapterService;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswerService;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestionService;
import com.unpi.educationalprojectbackend.User.User;
import com.unpi.educationalprojectbackend.User.UserService;
import com.unpi.educationalprojectbackend.UserProgress.UserProgress;
import com.unpi.educationalprojectbackend.UserProgress.UserProgressService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TestController extends ResponseHandler {
    public ChapterService chapterService;
    public SubChapterService subChapterService;
    public TestQuestionService testQuestionService;
    public TestAnswerService testAnswerService;
    public UserProgressService userProgressService;
    public UserService userService;
    public ChapterGradeService chapterGradeService;

    @GetMapping("test/{idChapter}/")
    public ResponseEntity<?> getTestDetails(@PathVariable Long idChapter) {

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

        List<TestQuestion> questionList = chapterService.getTestQuestions(chapter.getId());
        for (TestQuestion testQuestion:questionList) {
            List<TestAnswer> testAnswerList = testQuestionService.getTestAnswers(testQuestion);
            for (TestAnswer testAnswer:testAnswerList) {
                testAnswer.setQuestion(null);
                testAnswer.setQuestion(null);
            }
            testQuestion.setChapter(null);
            testQuestion.setAnswerList(testAnswerList);
        }

        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("chapter", chapterService.present(chapter));
        responseBody.put("questions", questionList);
        return createSuccessResponse(responseBody);
    }
}
