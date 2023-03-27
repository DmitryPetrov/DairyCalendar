package net.personal.dairycalendar.service;

import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.TagEntity;
import net.personal.dairycalendar.storage.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing 'update tags collection' method")
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    @DisplayName("Add tag to collection")
    void updateTagCollection_addTag() {
        CourseEntity entity = new CourseEntity();
        TagEntity tag1 = new TagEntity("tag_1");
        TagEntity tag2 = new TagEntity("tag_2");
        entity.getTagCollection().addTags(Set.of(tag1));
        assertEquals(Set.of(tag1), entity.getTagCollection().getTags(), "Collection must contains only one tag");
        when(tagRepository.findAllByTagIn(any())).thenReturn(Set.of(tag1, tag2));

        tagService.updateTagCollection(entity, Set.of("tag_1", "tag_2"));

        assertEquals(Set.of(tag1, tag2), entity.getTagCollection().getTags(), "Collection must contains two tag");
    }

    @Test
    @DisplayName("Remove tag from collection")
    void updateTagCollection_removeTag() {
        CourseEntity entity = new CourseEntity();
        TagEntity tag1 = new TagEntity("tag_1");
        TagEntity tag2 = new TagEntity("tag_2");
        entity.getTagCollection().addTags(Set.of(tag1, tag2));
        assertEquals(Set.of(tag1, tag2), entity.getTagCollection().getTags(), "Collection must contains two tag");
        when(tagRepository.findAllByTagIn(any())).thenReturn(Set.of(tag1));

        tagService.updateTagCollection(entity, Set.of("tag_1"));

        assertEquals(Set.of(tag1), entity.getTagCollection().getTags(), "Collection must contains only one tag");
    }

    @Test
    @DisplayName("Remove tag from collection and add tag to collection")
    void updateTagCollection_removeTagAndAddTag() {
        CourseEntity entity = new CourseEntity();
        TagEntity tag1 = new TagEntity("tag_1");
        TagEntity tag2 = new TagEntity("tag_2");
        TagEntity tag3 = new TagEntity("tag_3");
        entity.getTagCollection().addTags(Set.of(tag1, tag2));
        assertEquals(Set.of(tag1, tag2), entity.getTagCollection().getTags(),
                     "Collection must contains tag_1 and tag_2");
        when(tagRepository.findAllByTagIn(any())).thenReturn(Set.of(tag1, tag3));

        tagService.updateTagCollection(entity, Set.of("tag_1", "tag_3"));

        assertEquals(Set.of(tag1, tag3), entity.getTagCollection().getTags(),
                     "Collection must contains tag_1 and tag_3");
    }
}