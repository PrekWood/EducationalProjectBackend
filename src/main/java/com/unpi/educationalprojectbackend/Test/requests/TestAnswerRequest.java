package com.unpi.educationalprojectbackend.Test.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TestAnswerRequest {
    private Long idAnswer;
    private Long idQuestion;
}
