package net.personal.dairycalendar;

import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.repository.TagRepository;
import net.personal.dairycalendar.storage.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class AbstractTest {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private TaskRepository taskRepository;

    protected final static String USER_1_USERNAME = "user_1";
    protected final static String TAG_1_TITLE = "tag_1";
    protected final static String TAG_2_TITLE = "tag_2";
    protected final static String TAG_3_TITLE = "tag_3";
    protected final static String TAG_4_TITLE = "tag_4";
    protected final static String TAG_5_TITLE = "tag_5";

    public int getRandom() {
        return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    public AppUserEntity loadUser(String username) {
        return appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "There is no entity [" + AppUserEntity.class + "] " +
                                "with field [username] has value [" + username + "] in database"));
    }

    public CourseEntity saveCourse(String title, String username, Set<String> tags) {
        return saveCourse(title, username, tags, 10);
    }

    public CourseEntity saveCourse(String title, String username, Set<String> tags, int position) {
        CourseEntity courseEntity = new CourseEntity(
                title,
                "description",
                position,
                loadUser(username),
                tagRepository.findAllByTagIn(tags)
        );
        return courseRepository.save(courseEntity);
    }

    public DayEntity saveDay(long courseId, LocalDate day, int assessment) {
        CourseEntity courseEntity = courseRepository.findById(courseId)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, courseId));
        DayEntity dayEntity = new DayEntity(courseEntity, day, assessment);
        return dayRepository.save(dayEntity);
    }

    public TaskEntity saveTask(
            String username,
            Set<String> tags,
            boolean done,
            LocalDateTime finishedAt,
            TaskEntity parent
    ) {
        return saveTask("title", username, tags, done, finishedAt, parent);
    }

    public TaskEntity saveTask(
            String title,
            String username,
            Set<String> tags,
            boolean done,
            LocalDateTime finishedAt,
            TaskEntity parent
    ) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(title);
        entity.setDescription("description");
        entity.setPosition(1);
        entity.setPriority(1);
        entity.setDone(done);
        entity.setFinishedAt(finishedAt);
        entity.setUser(loadUser(username));
        entity.setParent(parent);
        entity.addTags(tagRepository.findAllByTagIn(tags));
        return taskRepository.save(entity);
    }

}
