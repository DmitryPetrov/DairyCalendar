package net.personal.dairycalendar.storage.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import net.personal.dairycalendar.storage.entity.AppUserEntity_;
import net.personal.dairycalendar.storage.entity.TagCollectionEntity_;
import net.personal.dairycalendar.storage.entity.TagEntity_;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import net.personal.dairycalendar.storage.entity.TaskEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.Set;

public class TaskSpecifications {

    public static Specification<TaskEntity> byUser(long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(TaskEntity_.USER).get(AppUserEntity_.ID), userId);
    }

    public static Specification<TaskEntity> byTitle(String title) {
        return (root, query, criteriaBuilder) -> Optional
                .ofNullable(title)
                .map(s -> criteriaBuilder.like(root.get(TaskEntity_.TITLE), "%" + s + "%"))
                .orElse(null);
    }

    public static Specification<TaskEntity> isDone(Boolean done) {
        return (root, query, criteriaBuilder) -> Optional
                .ofNullable(done)
                .map(b -> criteriaBuilder.equal(root.get(TaskEntity_.DONE), b))
                .orElse(null);
    }

    public static Specification<TaskEntity> isClosed(Boolean closed) {
        return (root, query, criteriaBuilder) -> Optional
                .ofNullable(closed)
                .map(b -> {
                    if (b) {
                        return criteriaBuilder.isNotNull(root.get(TaskEntity_.FINISHED_AT));
                    } else {
                        return criteriaBuilder.isNull(root.get(TaskEntity_.FINISHED_AT));
                    }
                })
                .orElse(null);
    }

    public static Specification<TaskEntity> hasTags(Set<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return null;
            }
            return criteriaBuilder
                    .in(root.get(TaskEntity_.TAG_COLLECTION).get(TagCollectionEntity_.TAGS).get(TagEntity_.TAG))
                    .value(tags);
        };
    }

    public static Specification<TaskEntity> hasNoTags(Set<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return null;
            }

            Subquery sub = query.subquery(Long.class);
            Root subRoot = sub.from(TaskEntity.class);
            Join tagCollection = subRoot.join(TaskEntity_.TAG_COLLECTION);
            Join tag = tagCollection.join(TagCollectionEntity_.TAGS);
            sub.select(subRoot.get(TaskEntity_.ID));
            sub.where(criteriaBuilder.in(tag.get(TagEntity_.TAG)).value(tags));
            return criteriaBuilder.not(root.get(TaskEntity_.ID).in(sub));
        };
    }
}
