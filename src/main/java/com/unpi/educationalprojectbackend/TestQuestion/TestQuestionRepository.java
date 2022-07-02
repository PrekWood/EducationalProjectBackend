package com.unpi.educationalprojectbackend.TestQuestion;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    @Query("FROM TestAnswer answer WHERE answer.question.id = ?1")
    List<TestAnswer> getTestAnswers(Long idQuestion);
}
