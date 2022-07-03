package com.unpi.educationalprojectbackend.Chapter;

import com.unpi.educationalprojectbackend.ChapterGrade.enums.GRADES;
import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.TestAttempt.TestAttempt;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateAdd;
    private String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    public Chapter(){
        this.dateAdd = new Date();
    }

    @Transient
    private List<SubChapter> subChapterList;
    @Transient
    private List<TestQuestion> testQuestions;
    @Transient
    private GRADES bestTestAttempt;
    @Transient
    private Float competionRate;
}
