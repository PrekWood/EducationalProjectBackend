package com.unpi.educationalprojectbackend.TestQuestion;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    @Query("FROM TestAnswer answer WHERE answer.question.id = ?1")
    List<TestAnswer> getTestAnswers(Long idQuestion);

    @Query("SELECT q.correctAnswer.id FROM TestQuestion q WHERE q.id = ?1")
    Long getCorrectAnswerOfQuestion(Long idQuestion);

    @Modifying
    @Transactional
    @Query("UPDATE TestAttemptAnswer a SET a.question = null WHERE a.question.id = ?1")
    void deleteFromTestAttempts(Long idQuestion);
}
