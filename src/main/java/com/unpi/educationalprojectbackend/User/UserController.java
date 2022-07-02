package com.unpi.educationalprojectbackend.User;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.Chapter.ChapterService;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.TYPE;
import com.unpi.educationalprojectbackend.SharedClasses.ResponseHandler;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.User.exceptions.EmailAlreadyBeingUsedUserException;
import com.unpi.educationalprojectbackend.User.requests.RegistrationRequest;
import com.unpi.educationalprojectbackend.UserProgress.UserProgress;
import com.unpi.educationalprojectbackend.UserProgress.UserProgressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/")
public class UserController extends ResponseHandler {

    private final UserService userService;
    private final UserProgressService userProgressService;
    private final ChapterService chapterService;


    @GetMapping("user")
    public ResponseEntity<?> getUserDetails(
            @RequestParam(required = false) String email
    ) {
        // Search for user
        User loggedInUser = userService.loadUserFromJwt();
        if (loggedInUser == null) {
            return createErrorResponse(HttpStatus.FORBIDDEN, "You are not loged in");
        }

        // Return details of logged in user
        if (email == null || email.equals("")) {
            return createSuccessResponse(userService.present(loggedInUser));
        }

        // Search user
        User userFromSearch;
        try {
            userFromSearch = userService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            return createErrorResponse("User not found");
        }
        return createSuccessResponse(userService.present(userFromSearch));
    }

    @PostMapping("user")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {

        // Check for empty fields
        if (
            request.getEmail() == null || request.getEmail().equals("") ||
            request.getPassword() == null || request.getPassword().equals("") ||
            request.getFirstName() == null || request.getFirstName().equals("") ||
            request.getLastName() == null || request.getLastName().equals("")
        ) {
            return createErrorResponse("Please fill in all the nesessary fields");
        }

        // Create new object
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setRole(UserRole.USER);

        // Try to sign in
        try {
            userService.signUpUser(newUser);
        } catch (EmailAlreadyBeingUsedUserException e) {
            return createErrorResponse(HttpStatus.CONFLICT, "E-mail already exists");
        }

        Chapter firstChapter = chapterService.getFirstChapter();

        // Create Progress
        UserProgress progress = new UserProgress();
        progress.setUser(newUser);
        progress.setNextObjectId(firstChapter == null ? null : firstChapter.getId());
        progress.setNextObjectType(TYPE.CHAPTER);
        userProgressService.save(progress);

        // Add Progress to user
        newUser.setProgress(progress);
        userService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.present(newUser));
    }


}

