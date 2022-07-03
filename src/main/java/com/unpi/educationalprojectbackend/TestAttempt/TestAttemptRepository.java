package com.unpi.educationalprojectbackend.TestAttempt;

import com.unpi.educationalprojectbackend.TestAttemptAnswer.TestAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt,Long> {

    @Query(
        "SELECT answer " +
        "FROM TestAttemptAnswer answer " +
        "WHERE answer.attempt.id = ?1 "
    )
    List<TestAttemptAnswer> getAnswers(Long idTestAttempt);
}
