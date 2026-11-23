package com.example.learning_platform;

import com.example.learning_platform.dto.CourseResponse;
import com.example.learning_platform.entity.Course;
import com.example.learning_platform.entity.Role;
import com.example.learning_platform.entity.Tag;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class TagServiceIT extends BaseIntegrationTest {
    @Autowired
    TagService tagService;

    @Test
    void tagLinkedToCourseIsFoundByTag() {
        Course c = createCourse(createUser(Role.TEACHER));
        Tag tag = tagService.create("tag-" + UUID.randomUUID());
        tagService.addTagToCourse(c.getId(), tag.getId());
        em.flush(); em.clear();

        assertThat(tagService.getCoursesByTag(tag.getId()))
                .extracting(CourseResponse::id).containsExactly(c.getId());
    }

    @Test
    void removedTagNoLongerFindsCourse() {
        Course c = createCourse(createUser(Role.TEACHER));
        Tag tag = tagService.create("tag-" + UUID.randomUUID());
        tagService.addTagToCourse(c.getId(), tag.getId());
        em.flush(); em.clear();

        tagService.removeTagFromCourse(c.getId(), tag.getId());
        em.flush(); em.clear();

        assertThat(tagService.getCoursesByTag(tag.getId())).isEmpty();
    }

    @Test
    void duplicateTagNameIsRejected() {
        String name = "tag-" + UUID.randomUUID();
        tagService.create(name);
        assertThatThrownBy(() -> tagService.create(name)).isInstanceOf(BusinessException.class);
    }
}