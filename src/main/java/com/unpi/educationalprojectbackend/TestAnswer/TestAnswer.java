package com.unpi.educationalprojectbackend.TestAnswer;

import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import com.unpi.educationalprojectbackend.TestQuestion.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class TestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateAdd;
    @OneToOne
    private TestQuestion question;
    private String answer;

    public TestAnswer(){
        this.dateAdd = new Date();
    }
}
