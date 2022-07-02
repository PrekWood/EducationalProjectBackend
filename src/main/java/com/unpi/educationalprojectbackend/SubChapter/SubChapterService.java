package com.unpi.educationalprojectbackend.SubChapter;

import com.unpi.educationalprojectbackend.Chapter.Chapter;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SubChapterService {
    private final SubChapterRepository repository;

    public SubChapter save(SubChapter subChapter) {
        return repository.save(subChapter);
    }

    public SubChapter find(Long id) throws ObjectNotFoundException {
        Optional<SubChapter> chapterFound = repository.findById(id);
        if (chapterFound.isEmpty()) {
            throw new ObjectNotFoundException(SubChapter.class, "not found");
        }
        return chapterFound.get();
    }

    public void delete(SubChapter subChapter) {
        repository.delete(subChapter);
    }
}

