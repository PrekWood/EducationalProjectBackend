package com.unpi.educationalprojectbackend.UserProgress;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.Chapter.ChapterService;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGradeService;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.TYPE;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapterService;
import com.unpi.educationalprojectbackend.Test.Test;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswerService;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestionService;
import com.unpi.educationalprojectbackend.User.User;
import com.unpi.educationalprojectbackend.User.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserProgressController extends ResponseHandler {

    public ChapterService chapterService;
    public SubChapterService subChapterService;
    public TestQuestionService testQuestionService;
    public TestAnswerService testAnswerService;
    public UserProgressService userProgressService;
    public UserService userService;
    public ChapterGradeService chapterGradeService;

    @PutMapping("progress/chapter/{idChapter}/")
    public ResponseEntity<?> markChapterAsRead(@PathVariable Long idChapter) {

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

        UserProgress progress = loggedInUser.getProgress();

        ChapterGrade chapterGrade = new ChapterGrade();
        chapterGrade.setIdObject(chapter.getId());
        chapterGrade.setUserProgress(progress);
        chapterGrade.setObjectType(TYPE.CHAPTER);
        chapterGrade.setGrade(GRADES.THREE);
        chapterGradeService.save(chapterGrade);

        getNextObjectAsCurrent(progress);

        return createSuccessResponse(chapterService.present(chapter));
    }

    @PutMapping("progress/test/{idChapter}/")
    public ResponseEntity<?> markTestAsRead(@PathVariable Long idChapter, @RequestParam GRADES grade) {

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

        UserProgress progress = loggedInUser.getProgress();

        ChapterGrade chapterGrade = new ChapterGrade();
        chapterGrade.setIdObject(chapter.getId());
        chapterGrade.setUserProgress(progress);
        chapterGrade.setObjectType(TYPE.TEST);
        chapterGrade.setGrade(grade);
        chapterGradeService.save(chapterGrade);

        boolean hasFinished = getNextObjectAsCurrent(progress);
        boolean alreadyShownModal = progress.isShowFinishedModal();
        boolean showFinishModal = hasFinished && !alreadyShownModal;
        if(showFinishModal){
            progress.setShowFinishedModal(true);
            userProgressService.save(progress);
        }
        return createSuccessResponse(showFinishModal);
    }

    @PutMapping("progress/subchapter/{idSubChapter}/")
    public ResponseEntity<?> markSubChapterAsRead(@PathVariable Long idSubChapter) {

        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "Δεν είστε συνδεδεμένος");
        }

        // Search for chapter
        SubChapter subChapter = null;
        try{
            subChapter = subChapterService.find(idSubChapter);
        }catch (ObjectNotFoundException e){
            return createErrorResponse("Το id του κεφαλαίου δεν είναι σωστό");
        }

        UserProgress progress = loggedInUser.getProgress();

        ChapterGrade chapterGrade = new ChapterGrade();
        chapterGrade.setIdObject(subChapter.getId());
        chapterGrade.setUserProgress(progress);
        chapterGrade.setObjectType(TYPE.SUBCHAPTER);
        chapterGrade.setGrade(GRADES.THREE);
        chapterGradeService.save(chapterGrade);

        getNextObjectAsCurrent(progress);

        return createSuccessResponse();
    }

    boolean getNextObjectAsCurrent(UserProgress progress){

        // Calculate next object
        boolean hasFinished = false;
        Object nextObject = chapterService.getNextInPath(progress);
        if(nextObject instanceof Chapter){
            progress.setNextObjectType(TYPE.CHAPTER);
            progress.setNextObjectId(((Chapter)nextObject).getId());
        } else if (nextObject instanceof SubChapter) {
            progress.setNextObjectType(TYPE.SUBCHAPTER);
            progress.setNextObjectId(((SubChapter)nextObject).getId());
        } else if (nextObject instanceof String && nextObject.equals("FINISH")) {
            progress.setNextObjectType(null);
            progress.setNextObjectId((long) -1);
            hasFinished = true;
        } else {
            progress.setNextObjectType(TYPE.TEST);
            progress.setNextObjectId(((Test)nextObject).getChapter().getId());
        }

        userProgressService.save(progress);

        return hasFinished;
    }
}
