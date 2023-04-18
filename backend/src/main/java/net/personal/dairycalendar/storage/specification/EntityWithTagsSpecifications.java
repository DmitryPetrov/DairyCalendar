package net.personal.dairycalendar.storage.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import net.personal.dairycalendar.storage.entity.EntityWithTags;
import net.personal.dairycalendar.storage.entity.EntityWithTags_;
import net.personal.dairycalendar.storage.entity.TagCollectionEntity_;
import net.personal.dairycalendar.storage.entity.TagEntity_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class EntityWithTagsSpecifications {

    public static <T> Specification<T> hasTags(Set<String> tags, Class<?> clazz) {
        return (root, query, criteriaBuilder) -> {
            if (!EntityWithTags.class.isAssignableFrom(clazz)) {
                throw new RuntimeException("Class [" + clazz.getName() + "] is not inherited from " + EntityWithTags.class);
            }
            if (tags == null || tags.isEmpty()) {
                return null;
            }
            return criteriaBuilder
                    .in(root.get(EntityWithTags_.TAG_COLLECTION).get(TagCollectionEntity_.TAGS).get(TagEntity_.TAG))
                    .value(tags);
        };
    }

    public static <T> Specification<T> hasNoTags(Set<String> tags, Class<?> clazz) {
        return (root, query, criteriaBuilder) -> {
            if (!EntityWithTags.class.isAssignableFrom(clazz)) {
                throw new RuntimeException("Class [" + clazz.getName() + "] is not inherited from " + EntityWithTags.class);
            }
            if (tags == null || tags.isEmpty()) {
                return null;
            }
            Subquery sub = query.subquery(Long.class);
            Root subRoot = sub.from(clazz);
            Join tagCollection = subRoot.join(EntityWithTags_.TAG_COLLECTION);
            Join tag = tagCollection.join(TagCollectionEntity_.TAGS);
            sub.select(subRoot.get(EntityWithTags_.ID));
            sub.where(criteriaBuilder.in(tag.get(TagEntity_.TAG)).value(tags));
            return criteriaBuilder.not(root.get(EntityWithTags_.ID).in(sub));
        };
    }
}
