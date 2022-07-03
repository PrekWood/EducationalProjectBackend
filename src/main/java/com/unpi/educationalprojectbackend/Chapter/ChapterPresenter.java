package com.unpi.educationalprojectbackend.Chapter;

import com.unpi.educationalprojectbackend.SharedClasses.ModelPresenter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.SubChapter.SubChapterService;
import com.unpi.educationalprojectbackend.User.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class ChapterPresenter implements ModelPresenter<Chapter> {


    @Override
    public Object present(Chapter chapter) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        HashMap<String, Object> presented = new HashMap<>();
        presented.put("id",chapter.getId());
        presented.put("name",chapter.getName());
        presented.put("description",chapter.getDescription());
        presented.put("dateAdd",formatter.format(chapter.getDateAdd()));
        presented.put("subChapters",chapter.getSubChapterList());
        presented.put("testQuestions",chapter.getTestQuestions());
        if(chapter.getBestTestAttempt()!=null){
            presented.put("bestAttempt",chapter.getBestTestAttempt());
        }else{
            presented.put("bestAttempt",null);
        }
        if(chapter.getCompetionRate()!=null){
            presented.put("completionRate",chapter.getCompetionRate());
        }else{
            presented.put("completionRate",null);
        }

        return presented;
    }

    @Override
    public Object presentMultiple(List<Chapter> objectList) {
        List<Object> listToReturn = new ArrayList<>();
        for (Chapter chapter : objectList) {
            listToReturn.add(present(chapter));
        }
        return listToReturn;
    }
}
