package com.unpi.educationalprojectbackend.Chapter;

import com.unpi.educationalprojectbackend.Chapter.requests.ChapterCreateRequest;
import com.unpi.educationalprojectbackend.Chapter.requests.ChapterRenameRequest;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGradeService;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapterService;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswerService;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestionService;
import com.unpi.educationalprojectbackend.User.User;
import com.unpi.educationalprojectbackend.User.UserRole;
import com.unpi.educationalprojectbackend.User.UserService;
import com.unpi.educationalprojectbackend.User.exceptions.EmailAlreadyBeingUsedUserException;
import com.unpi.educationalprojectbackend.User.requests.RegistrationRequest;
import com.unpi.educationalprojectbackend.UserProgress.UserProgress;
import com.unpi.educationalprojectbackend.UserProgress.UserProgressService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.spi.CharsetProvider;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ChapterController extends ResponseHandler {

    public ChapterService chapterService;
    public SubChapterService subChapterService;
    public TestQuestionService testQuestionService;
    public TestAnswerService testAnswerService;
    public UserProgressService userProgressService;
    public UserService userService;
    public ChapterGradeService chapterGradeService;

    @GetMapping("chapters")
    public ResponseEntity<?> getAllChapters() {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "You are not loged in");
        }

        List<Chapter> chapterList = chapterService.getAll();
        return createSuccessResponse(chapterService.present(chapterList));
    }

    @GetMapping("chapter/next-name")
    public ResponseEntity<?> getNewChapterName() {
        return createSuccessResponse(
            String.valueOf(chapterService.getCount()+1) + "ο Κεφάλαιο"
        );
    }

    @PostMapping("chapter")
    public ResponseEntity<?> createChapter(@RequestBody ChapterCreateRequest request) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "You are not loged in");
        }

        Chapter chapter = new Chapter();
        chapter.setName(request.getName());
        chapter.setDescription(request.getDescription());
        chapterService.save(chapter);

        return createSuccessResponse(chapterService.present(chapter));
    }



    @PutMapping ("chapter/{idChapter}")
    public ResponseEntity<?> rename(@RequestParam String newName, @PathVariable Long idChapter) {

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

        chapter.setName(newName);
        chapterService.save(chapter);

        return createSuccessResponse(chapterService.present(chapter));
    }

    @PutMapping ("chapter/{idChapter}/full")
    public ResponseEntity<?> update(@RequestBody ChapterCreateRequest request, @PathVariable Long idChapter) {

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

        chapter.setName(request.getName());
        chapter.setDescription(request.getDescription());
        chapterService.save(chapter);

        return createSuccessResponse(chapterService.present(chapter));
    }



    @DeleteMapping ("chapter/{idChapter}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long idChapter) {

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

        // Delete Subchapters
        List<SubChapter> subChapterList = chapterService.getSubchapters(chapter.getId());
        for (SubChapter subChapter:subChapterList) {
            subChapterService.delete(subChapter);
        }

        // Update current chapter on userprogress
        userProgressService.replaceChapter(chapter.getId(), null);

        // Delete from passed chapters
        userProgressService.deleteFromPassedChapters(chapter.getId());

        // Delete Test Questions and Answers
        List<TestQuestion> testQuestionList = chapterService.getTestQuestions(chapter.getId());
        for (TestQuestion testQuestion:testQuestionList) {
            List<TestAnswer> testAnswerList = testQuestionService.getTestAnswers(testQuestion);
            for (TestAnswer testAnswer:testAnswerList) {
                testAnswerService.delete(testAnswer);
            }
            testQuestionService.delete(testQuestion);
        }

        chapterService.delete(chapter);
        return createSuccessResponse();
    }

    @GetMapping ("chapter/{idChapter}")
    public ResponseEntity<?> getChapterDetails(@PathVariable Long idChapter) {

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

        return createSuccessResponse(chapterService.present(chapter));
    }


}
