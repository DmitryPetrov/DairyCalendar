package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.storage.entity.EntityWithTags;
import net.personal.dairycalendar.storage.entity.TagEntity;
import net.personal.dairycalendar.storage.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<String> getTags() {
        return tagRepository.findAll()
                .stream()
                .map(TagEntity::getTag)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTagCollection(EntityWithTags entity, Set<String> tags) {
        Set<TagEntity> persistedTags = tagRepository.findAllByTagIn(tags);
        Set<TagEntity> notPersistedTags = tags
                .stream()
                .map(TagEntity::new)
                .collect(Collectors.toSet());
        notPersistedTags.removeIf(newTag -> persistedTags
                .stream()
                .map(TagEntity::getTag)
                .anyMatch(existedTag -> existedTag.equalsIgnoreCase(newTag.getTag())));
        tagRepository.saveAll(notPersistedTags);
        Set<TagEntity> newTags = new HashSet<>();
        newTags.addAll(persistedTags);
        newTags.addAll(notPersistedTags);
        entity.updateTags(newTags);
    }
}
