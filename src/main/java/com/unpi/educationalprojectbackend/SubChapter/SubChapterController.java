package com.unpi.educationalprojectbackend.SubChapter;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.Chapter.ChapterService;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.SubChapter.requests.SubChapterCreateRequest;
import com.unpi.educationalprojectbackend.SubChapter.requests.SubChapterUpdateRequest;
import com.unpi.educationalprojectbackend.User.User;
import com.unpi.educationalprojectbackend.User.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SubChapterController extends ResponseHandler {

    public ChapterService chapterService;
    public SubChapterService subChapterService;
    public UserService userService;

    @PostMapping("chapter/{idChapter}/subchapter")
    public ResponseEntity<?> createSubChapter(
            @PathVariable Long idChapter,
            @RequestBody SubChapterCreateRequest requestBody
    ) {

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


        // Create subchapter
        SubChapter subChapter = new SubChapter();
        subChapter.setChapter(chapter);
        subChapter.setName(requestBody.getName());
        subChapter.setTheory(requestBody.getTheory());
        subChapter.setExamples(requestBody.getExamples());
        subChapterService.save(subChapter);

        return createSuccessResponse(chapterService.present(chapter));
    }

    @PutMapping("subchapter/{idSubChapter}/")
    public ResponseEntity<?> rename(
            @RequestBody SubChapterCreateRequest requestBody,
            @PathVariable Long idSubChapter
    ) {

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
        subChapter.setName(requestBody.getName());
        subChapter.setTheory(requestBody.getTheory());
        subChapter.setExamples(requestBody.getExamples());
        subChapterService.save(subChapter);


        System.out.println(requestBody.getTheory());

        return createSuccessResponse(subChapter);
    }

    @DeleteMapping("subchapter/{idSubChapter}")
    public ResponseEntity<?> delete(
            @PathVariable Long idSubChapter
    ) {

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
        subChapterService.delete(subChapter);
        return createSuccessResponse();
    }

    @GetMapping("subchapter/{idSubChapter}")
    public ResponseEntity<?> getDetails(
            @PathVariable Long idSubChapter
    ) {

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

        return createSuccessResponse(subChapter);
    }

}
