package net.personal.dairycalendar.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class TaskDto {

    private long id;
    private String title;
    private String description;
    private int position;
    private int priority;
    private boolean done;
    private LocalDateTime finishedAt;
    private Long parentId;
    private Set<String> tags = new HashSet<>();
    private TaskDto parent;
    private List<TaskDto> children = new ArrayList<>();

    public void addTags(Set<String> tags) {
        this.tags.addAll(tags);
    }
}
