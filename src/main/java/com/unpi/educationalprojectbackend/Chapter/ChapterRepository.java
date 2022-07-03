package com.unpi.educationalprojectbackend.Chapter;

import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query("SELECT count(chapter) FROM Chapter chapter")
    Long getCount();

    @Query("SELECT count(subChapter) FROM SubChapter subChapter WHERE subChapter.chapter.id = ?1")
    Long getSubChaptersCount(Long idChapter);

    @Query(
        value = "SELECT * " +
            "FROM chapter c " +
            "WHERE c.id = (SELECT min(c.id) FROM chapter c)",
        nativeQuery = true
    )
    Chapter getFirstChapter();

    @Query("FROM SubChapter subchapter WHERE subchapter.chapter.id = ?1 ORDER BY subchapter.id ASC")
    List<SubChapter> getSubChapters(Long idChapter);

    @Query("FROM TestQuestion question WHERE question.chapter.id = ?1")
    List<TestQuestion> getTestQuestions(Long idChapter);
    @Query(
        value = "select grade " +
                "from test_attempt " +
                "where chapter_id = ?1 " +
                "and user_id = ?2 " +
                "and id = ( " +
                "    select id " +
                "    from test_attempt " +
                "    where chapter_id = ?1 " +
                "    and user_id = ?2 " +
                "    and percentage = ( " +
                "        select max(percentage) " +
                "        from test_attempt " +
                "        where chapter_id = ?1 " +
                "        and user_id = ?2 " +
                "    )" +
                "   limit 1 " +
                ")",
        nativeQuery = true
    )
//    @Query(
//        value = "" +
//            "SELECT attempt " +
//            "FROM TestAttempt attempt " +
//            "WHERE attempt.chapter.id = ?1 " +
//            "AND attempt.user.id = ?2 " +
//            "AND attempt.id = ( " +
//            "    SELECT bestAttempt.id " +
//            "    FROM TestAttempt bestAttempt" +
//            "    WHERE bestAttempt.chapter.id = ?1  " +
//            "    AND bestAttempt.user.id = ?2 " +
//            "    AND bestAttempt.percentage = ( " +
//            "        SELECT max(maxPercentageAttempt.percentage) " +
//            "        FROM TestAttempt maxPercentageAttempt" +
//            "        WHERE maxPercentageAttempt.chapter.id = ?1 " +
//            "        AND maxPercentageAttempt.user.id = ?2 " +
//            "    ) " +
//            ")"
//    )
    GRADES getBestTestAttempt(Long idChapter, Long idUser);

    @Query(
            value = "select  " +
                    "    (  " +
                    "        select count(*)  " +
                    "        from test_attempt  " +
                    "        where chapter_id = ?1  " +
                    "          and percentage > 0.5  " +
                    "    )  " +
                    "        /  " +
                    "    (  " +
                    "        select count(*)  " +
                    "        from test_attempt  " +
                    "        where chapter_id = ?1  " +
                    "    )*100  " +
                    "as completionRate  ",
            nativeQuery = true
    )
    Float getCompletionRate(Long idChapter);
}
