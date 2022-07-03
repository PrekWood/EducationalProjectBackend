package com.unpi.educationalprojectbackend.SubChapter;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import com.unpi.educationalprojectbackend.TestQuestion.TestQuestion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class SubChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String theory;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String examples;
    private String name;

    @OneToOne
    private Chapter chapter;
}
