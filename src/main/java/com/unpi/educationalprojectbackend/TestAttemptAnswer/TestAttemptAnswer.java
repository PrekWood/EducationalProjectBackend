package com.unpi.educationalprojectbackend.TestAttemptAnswer;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttempt;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class TestAttemptAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private TestAttempt attempt;
    @OneToOne
    private TestQuestion question;
    @OneToOne
    private TestAnswer answer;
    private boolean isCorrect;

}
