package com.unpi.educationalprojectbackend.Chapter.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChapterCreateRequest {
    private String name;
    private String description;
}
