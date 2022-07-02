package com.unpi.educationalprojectbackend.Chapter;

import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.Test.Test;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import com.unpi.educationalprojectbackend.UserProgress.UserProgress;
import com.unpi.educationalprojectbackend.UserProgress.UserProgressService;
import lombok.AllArgsConstructor;
import org.hibernate.Cache;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChapterService {

    public ChapterRepository repository;
    public ChapterPresenter presenter;
    public UserProgressService userProgressService;

    public List<Chapter> getAll(){
        return repository.findAll();
    }

    public Chapter save(Chapter chapter){
        return repository.save(chapter);
    }

    public Long getCount(){
        return repository.getCount();
    }
    public Object present(Chapter chapter){
        chapter.setSubChapterList(getSubchapters(chapter.getId()));
        chapter.setTestQuestions(getTestQuestions(chapter.getId()));
        return presenter.present(chapter);
    }
    public Object present(List<Chapter> cl){
        for (Chapter c: cl) {
            c.setSubChapterList(getSubchapters(c.getId()));
            c.setTestQuestions(getTestQuestions(c.getId()));
        }
        return presenter.presentMultiple(cl);
    }
    public Chapter find(Long id) throws ObjectNotFoundException{
        Optional<Chapter> chapterFound = repository.findById(id);
        if(chapterFound.isEmpty()){
            throw new ObjectNotFoundException(Chapter.class,"not found");
        }
        return chapterFound.get();
    }

    public void delete(Chapter chapter){
        repository.delete(chapter);
    }

    public Long getSubChaptersCount(Chapter chapter){
        return repository.getSubChaptersCount(chapter.getId());
    }

    public List<SubChapter> getSubchapters(Long idChapter) {
        List<SubChapter> subChapterList = repository.getSubChapters(idChapter);
        for (SubChapter subChapter : subChapterList) {
            subChapter.setChapter(null);
        }
        return subChapterList;
    }

    public List<TestQuestion> getTestQuestions(Long idChapter) {
        List<TestQuestion> testQuestionList = repository.getTestQuestions(idChapter);
        for (TestQuestion testQuestion : testQuestionList) {
            testQuestion.setChapter(null);
        }

        return testQuestionList;
    }

    public Chapter getFirstChapter(){
        return repository.getFirstChapter();
    }

    public Test getTest(Chapter c){
        Test t = new Test();
        t.setChapter(c);
        return t;
    }

    public Object getNextInPath(UserProgress progress){
        List<Chapter> chapterList = getAll();

        for (Chapter chapter: chapterList) {

            // TODO check if intro is readed

            // Get the first subchapter that is not passed
            List<SubChapter> subChapterList = repository.getSubChapters(chapter.getId());
            for (SubChapter subChapter: subChapterList) {
                if(!userProgressService.isSubChapterPassed(subChapter.getId(), progress.getId())){
                    return subChapter;
                }
            }

            // Check if the test is passed
            if(!userProgressService.isChapterTestPassed(chapter.getId(), progress.getId())){
                return getTest(chapter);
            }
        }

        // Get the next chapter
        return null;
    }

}
