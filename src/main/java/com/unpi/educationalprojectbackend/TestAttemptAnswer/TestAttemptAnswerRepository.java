package com.unpi.educationalprojectbackend.TestAttemptAnswer;

import com.unpi.educationalprojectbackend.TestAttempt.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestAttemptAnswerRepository extends JpaRepository<TestAttemptAnswer,Long> {

}
