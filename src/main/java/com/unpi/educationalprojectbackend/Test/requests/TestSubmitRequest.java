package com.unpi.educationalprojectbackend.Test.requests;

import com.unpi.educationalprojectbackend.TestAnswer.TestAnswer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestSubmitRequest {
    private List<TestAnswerRequest> answers;
}
