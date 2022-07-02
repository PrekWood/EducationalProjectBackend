package com.unpi.educationalprojectbackend.Chapter;

import com.unpi.educationalprojectbackend.SubChapter.SubChapter;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import jdk.jfr.Enabled;
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
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateAdd;
    private String name;
    @Lob
    private String description;
    public Chapter(){
        this.dateAdd = new Date();
    }

    @Transient
    private List<SubChapter> subChapterList;
    @Transient
    private List<TestQuestion> testQuestions;
}
