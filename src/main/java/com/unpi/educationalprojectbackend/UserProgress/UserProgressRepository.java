package com.unpi.educationalprojectbackend.UserProgress;

import com.unpi.educationalprojectbackend.ChapterGrade.ChapterGrade;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttempt;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionErrorType;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    @Query("FROM ChapterGrade cg WHERE cg.userProgress.id = ?1")
    List<ChapterGrade> getPassedChapters(Long idUserProgress);

    @Modifying
    @Transactional
    @Query("UPDATE UserProgress progress SET progress.nextObjectId = ?2 WHERE progress.nextObjectId= ?1")
    void replaceChapter(Long idChapter, Long idChapterToReplace);


    @Modifying
    @Transactional
    @Query("DELETE FROM ChapterGrade cg WHERE cg.idObject = ?1")
    void deleteFromPassedChapters(Long idChapter);

    @Query(
        "SELECT count(cg) " +
        "FROM ChapterGrade cg " +
        "WHERE cg.idObject = ?1 " +
        "AND cg.objectType = 1 " +
        "AND cg.userProgress.id = ?2"
    )
    Long getCountOfSubChapterPassed(Long idSubChapter, Long idProgress);

    @Query(
        "SELECT count(cg) " +
        "FROM ChapterGrade cg " +
        "WHERE cg.idObject = ?1 " +
        "AND cg.objectType = 2 " +
        "AND cg.userProgress.id = ?2"
    )
    Long getCountOfChapterTestPassed(Long idChapter, Long idProgress);

    @Query(
        "SELECT count(cg) " +
        "FROM ChapterGrade cg " +
        "WHERE cg.idObject = ?1 " +
        "AND cg.objectType = 0 " +
        "AND cg.userProgress.id = ?2"
    )
    Long getCountOfChapterIntroPassed(Long idChapter, Long idProgress);

    @Query(
        "SELECT attempt " +
        "FROM TestAttempt attempt " +
        "LEFT JOIN User user ON user.id = attempt.user.id "+
        "WHERE attempt.chapter.id = ?1 " +
        "AND user.progress.id = ?2"
    )
    List<TestAttempt> getPreviousTestAttempts(Long idChapter, Long idProgress);
    @Query(
        value = "select question.error_type, count(*) as errorCount  " +
                "from test_attempt_answer answer  " +
                "left join test_attempt attempt on answer.attempt_id = attempt.id  " +
                "left join test_question question on question.id = answer.question_id  " +
                "where user_id = ?1  " +
                "and answer.is_correct = false  " +
                "group by question.error_type  " +
                "order by count(*) desc  " +
                "limit 1  ",
        nativeQuery = true
    )
    QuestionErrorType getTypeOfMostErrors(Long idUser);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE user_progress " +
                "SET next_object_id = ?2, " +
                "next_object_type = ?3 " +
                "WHERE id = ?1",
            nativeQuery = true
    )
    void updateProgress(Long idProgress, Long idObject, int objectType);
}
