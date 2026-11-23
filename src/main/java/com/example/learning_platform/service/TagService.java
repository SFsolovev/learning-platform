package com.example.learning_platform.service;

import com.example.learning_platform.dto.CourseResponse;
import com.example.learning_platform.entity.Course;
import com.example.learning_platform.entity.CourseRepository;
import com.example.learning_platform.entity.Tag;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.exception.NotFoundException;
import com.example.learning_platform.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Tag create(String name) {
        if (tagRepository.findByName(name).isPresent()) {
            throw new BusinessException("Тег уже существует: " + name);
        }
        Tag t = new Tag();
        t.setName(name);
        return tagRepository.save(t);
    }

    @Transactional(readOnly = true)
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Transactional
    public void addTagToCourse(Long courseId, Long tagId) {
        getCourse(courseId).getTags().add(getTag(tagId));   // владелец связи: Course
    }

    @Transactional
    public void removeTagFromCourse(Long courseId, Long tagId) {
        getCourse(courseId).getTags().remove(getTag(tagId));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByTag(Long tagId) {
        getTag(tagId);
        return courseRepository.findByTagsId(tagId).stream().map(CourseResponse::from).toList();
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Курс не найден: " + id));
    }

    private Tag getTag(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Тег не найден: " + id));
    }
}