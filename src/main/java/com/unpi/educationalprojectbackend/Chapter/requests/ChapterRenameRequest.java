package com.unpi.educationalprojectbackend.Chapter.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChapterRenameRequest {
    private String name;
}
