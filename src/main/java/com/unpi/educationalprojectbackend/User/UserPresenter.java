package com.unpi.educationalprojectbackend.User;

import com.unpi.educationalprojectbackend.SharedClasses.ModelPresenter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.UserProgress.UserProgress;
import com.unpi.educationalprojectbackend.UserProgress.UserProgressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class UserPresenter implements ModelPresenter<User> {

    private UserProgressService userProgressService;
    @Override
    public HashMap<String, Object> present(User user) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("id",user.getId());
        response.put("email",user.getEmail());
        response.put("firstName",user.getFirstName());
        response.put("lastName",user.getLastName());
        response.put("isAdmin", user.getRole() == UserRole.ADMIN);
        response.put("progress", userProgressService.present(user.getProgress()));

        return response;
    }

    @Override
    public List<HashMap<String, Object>> presentMultiple(List<User> userList) {
        List<HashMap<String, Object>> userListToReturn = new ArrayList<>();
        for (User user : userList) {
            userListToReturn.add(present(user));
        }
        return userListToReturn;
    }

}


