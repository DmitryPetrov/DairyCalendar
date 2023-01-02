package net.personal.dairycalendar.storage.repository;

import net.personal.dairycalendar.storage.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Set<TagEntity> findAllByTagIn(Set<String> tagNames);

}
