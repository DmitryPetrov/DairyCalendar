package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.AppUserEntity_;
import net.personal.dairycalendar.storage.entity.CourseEntity_;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<TaskEntity> byUser(long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(CourseEntity_.USER).get(AppUserEntity_.ID), userId);
    }

/*    public static Specification<CourseEntity> hasTags(Set<String> tags) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .in(root.get(CourseEntity_.TAGS).get(TagEntity_.TAG))
                    .value(tags);
        };
    }*/
}
