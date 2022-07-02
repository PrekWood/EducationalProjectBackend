package com.unpi.educationalprojectbackend.UserProgress;


import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.TYPE;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.User.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserProgressService {
    private final UserProgressRepository repository;
    private final UserProgressPresenter presenter;

    public UserProgress save(UserProgress subChapter) {
        return repository.save(subChapter);
    }

    public List<ChapterGrade> getPassedChapters(Long idUserProgress){
        return repository.getPassedChapters(idUserProgress);
    }
    public void replaceChapter(Long idChapter,Long idChapterToReplace){
        repository.replaceChapter(idChapter, idChapterToReplace);
    }
    public void deleteFromPassedChapters(Long idChapter){
        repository.deleteFromPassedChapters(idChapter);
    }

    public Object present(UserProgress userProgress){
        List<ChapterGrade> chaptersPassed = getPassedChapters(userProgress.getId());
        for (ChapterGrade chapter : chaptersPassed) {
            chapter.setUserProgress(null);
        }
        userProgress.setChaptersPassed(chaptersPassed);
        return presenter.present(userProgress);
    }

    public boolean isSubChapterPassed(Long idSubChapter, Long idProgress){
        return repository.getCountOfSubChapterPassed(idSubChapter, idProgress) > 0;
    }
    public boolean isChapterTestPassed(Long idChapter, Long idProgress){
        return repository.getCountOfChapterTestPassed(idChapter, idProgress) > 0;
    }

}
