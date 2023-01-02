package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.storage.entity.TagEntity;
import net.personal.dairycalendar.storage.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Set<TagEntity> getTags(Set<String> tagNames) {
        Set<TagEntity> result = tagRepository.findAllByTagIn(tagNames);
        Set<TagEntity> newTags = tagNames
                .stream()
                .filter(tagName -> result.stream().noneMatch(tag -> tag.getTag().equals(tagName)))
                .map(TagEntity::new)
                .collect(Collectors.toSet());
        tagRepository.saveAll(newTags);
        result.addAll(newTags);
        return result;
    }
}
