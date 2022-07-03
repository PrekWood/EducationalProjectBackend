package com.unpi.educationalprojectbackend.UserProgress;


import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttempt;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttemptService;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionErrorType;
import com.unpi.educationalprojectbackend.User.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserProgressService {
    private final UserProgressRepository repository;
    private final UserProgressPresenter presenter;
    private final TestAttemptService testAttemptService;

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
    public boolean isChapterIntroPassed(Long idChapter, Long idProgress){
        return repository.getCountOfChapterIntroPassed(idChapter, idProgress) > 0;
    }
    public boolean isChapterTestPassed(Long idChapter, Long idProgress){
        return repository.getCountOfChapterTestPassed(idChapter, idProgress) > 0;
    }

    public List<Object> getPreviousTestAttempts(Long idChapter, Long idProgress){
        List<TestAttempt> testAttempts = repository.getPreviousTestAttempts(idChapter, idProgress);
        return testAttemptService.present(testAttempts);
    }
    public QuestionErrorType getTypeOfMostErrors(User user){
        return repository.getTypeOfMostErrors(user.getId());
    }

}
