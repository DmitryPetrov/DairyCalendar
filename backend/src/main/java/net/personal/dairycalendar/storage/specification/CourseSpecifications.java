package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.AppUserEntity_;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class CourseSpecifications {

    public static Specification<CourseEntity> fromUser(long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(CourseEntity_.USER).get(AppUserEntity_.ID), userId);
    }

    public static Specification<CourseEntity> fromUsers(Set<Long> usersIdCollection) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .in(root.get(CourseEntity_.USER).get(AppUserEntity_.ID))
                .value(usersIdCollection);
    }

/*    public static Specification<CourseEntity> hasTags(Set<String> tags) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .in(root.get(CourseEntity_.TAGS).get(TagEntity_.TAG))
                    .value(tags);
        };
    }*/
}
