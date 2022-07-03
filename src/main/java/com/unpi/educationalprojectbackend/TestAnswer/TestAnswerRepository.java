package com.unpi.educationalprojectbackend.TestAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TestAnswerRepository extends JpaRepository<TestAnswer,Long> {


    @Modifying
    @Transactional
    @Query("UPDATE TestQuestion q SET q.correctAnswer = null WHERE q.correctAnswer.id= ?1")
    void removeFromCorrectAnswerForeignKey(Long idAnswer);

    @Modifying
    @Transactional
    @Query("DELETE FROM TestAttemptAnswer a WHERE a.answer.id= ?1")
    void removeFromTestAttemptForeignKey(Long idAnswer);

}
