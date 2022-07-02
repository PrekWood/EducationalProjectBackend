package com.unpi.educationalprojectbackend.UserProgress;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.SharedClasses.ModelPresenter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class UserProgressPresenter implements ModelPresenter<UserProgress> {

    @Override
    public Object present(UserProgress progress) {
        HashMap<String, Object> response = new HashMap<>();

        response.put("nextObjectId", progress.getNextObjectId());
        response.put("nextObjectType", progress.getNextObjectType());
        response.put("chaptersPassed", progress.getChaptersPassed());

        return response;
    }

    @Override
    public Object presentMultiple(List<UserProgress> objectList) {
        return null;
    }
}
