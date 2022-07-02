package com.unpi.educationalprojectbackend.SubChapter.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubChapterCreateRequest {
    private String name;
    private String theory;
    private String examples;
}
