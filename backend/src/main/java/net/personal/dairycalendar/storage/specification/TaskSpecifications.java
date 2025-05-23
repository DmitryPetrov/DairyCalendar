package net.personal.dairycalendar.storage.specification;

import net.personal.dairycalendar.storage.entity.AppUserEntity_;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import net.personal.dairycalendar.storage.entity.TaskEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

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

    public static Specification<TaskEntity> byParent(long parentTaskId) {
        return (root, query, criteriaBuilder) -> Optional
                .ofNullable(parentTaskId)
                .map(id -> criteriaBuilder.equal(root.get(TaskEntity_.PARENT).get(TaskEntity_.ID), id))
                .orElse(null);
    }
}
