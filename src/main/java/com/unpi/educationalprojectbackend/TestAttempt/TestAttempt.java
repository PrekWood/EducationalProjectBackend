package com.unpi.educationalprojectbackend.TestAttempt;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.TestAttemptAnswer.TestAttemptAnswer;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import com.unpi.educationalprojectbackend.User.User;
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
public class TestAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateAdd;

    @OneToOne
    private Chapter chapter;

    @OneToOne
    private User user;

    private GRADES grade;
    private Float percentage;

    public TestAttempt(){
        this.dateAdd = new Date();
    }

    @Transient
    private List<TestAttemptAnswer> answers;
}
