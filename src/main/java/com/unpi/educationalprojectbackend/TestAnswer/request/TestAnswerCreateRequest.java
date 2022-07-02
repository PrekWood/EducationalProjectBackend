package com.unpi.educationalprojectbackend.TestAnswer.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TestAnswerCreateRequest {
    private String answer;
    private boolean correct;
}
