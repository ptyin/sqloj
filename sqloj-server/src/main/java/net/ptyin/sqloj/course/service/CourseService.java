package net.ptyin.sqloj.course.service;

import net.ptyin.sqloj.course.CourseDto;
import net.ptyin.sqloj.course.CourseEntity;
import net.ptyin.sqloj.course.CourseNotFoundException;
import net.ptyin.sqloj.user.UserEntity;

import java.util.List;
import java.util.UUID;

public interface CourseService
{
    List<CourseEntity> getUserParticipatedCourseList(UUID userUuid);
    List<UserEntity> getParticipatorList(UUID courseUuid) throws CourseNotFoundException;

    CourseEntity findCourse(UUID courseUuid) throws CourseNotFoundException;
    CourseEntity findCourseEagerly(UUID courseUuid) throws CourseNotFoundException;
    CourseEntity openCourse(CourseDto courseDto, List<UserEntity> participatorList);
    CourseEntity updateCourse(CourseEntity entity, CourseDto courseDto, List<UserEntity> participatorList);
    void saveCourse(CourseEntity course);
    void deleteCourse(CourseEntity course);
    void deleteCourse(UUID courseUuid);
}
