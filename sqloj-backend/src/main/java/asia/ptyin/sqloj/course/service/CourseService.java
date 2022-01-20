package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.user.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface CourseService
{
    List<CourseEntity> getUserParticipatedCourseList(UUID userUuid);
    List<UserEntity> getParticipatorList(UUID courseUuid);
    CourseEntity findCourse(UUID courseUuid);
    CourseEntity openCourse(CourseDto courseDto, List<UserEntity> participatorList);
    void saveCourse(CourseEntity course);
    void deleteCourse(CourseEntity course);
}
