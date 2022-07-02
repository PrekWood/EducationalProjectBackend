package com.unpi.educationalprojectbackend.TestQuestion;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionDifficulty;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionErrorType;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class TestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateAdd;
    private String question;
    private QuestionType type;
    private QuestionDifficulty difficulty;
    private QuestionErrorType errorType;
    @OneToOne
    private Chapter chapter;
    @OneToOne
    private TestAnswer correctAnswer;


    public TestQuestion() {
        this.dateAdd = new Date();
    }


    @Transient
    private List<TestAnswer> answerList;
}
