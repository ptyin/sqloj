package net.ptyin.sqloj.course.controllers;

import net.ptyin.sqloj.course.CourseDto;
import net.ptyin.sqloj.course.service.CourseService;
import net.ptyin.sqloj.user.UserEntity;
import net.ptyin.sqloj.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequestMapping("/course")
@RestController
public class CourseController
{

    private UserService userService;
    private CourseService courseService;

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    @GetMapping("/{courseUuid}")
    public CourseDto get(@PathVariable UUID courseUuid)
    {
        var course = courseService.findCourseEagerly(courseUuid);
        var participatorList = course.getParticipatorList().stream().map(UserEntity::getUuid).toList();
        return new CourseDto(course.getName(), course.getDescription(), course.getStartedAt(), course.getEndedAt(), participatorList);
    }

    /**
     * Open a course.
     */
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public Map<String, Object> open(@Valid @RequestBody CourseDto courseDto)
    {
        var result = new HashMap<String, Object>();
        var participatorList = userService.findAllUser(courseDto.getParticipatorList());
        courseService.openCourse(courseDto, participatorList);

        result.put("success", true);
        return result;
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{courseUuid}")
    public Map<String, Object> update(@PathVariable UUID courseUuid, @Valid @RequestBody CourseDto courseDto)
    {
        var result = new HashMap<String, Object>();
        var participatorList = userService.findAllUser(courseDto.getParticipatorList());
        var course = courseService.findCourse(courseUuid);
        courseService.updateCourse(course, courseDto, participatorList);
        result.put("success", true);

        return result;
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @DeleteMapping("/{courseUuid}")
    public Map<String, Object>delete(@PathVariable UUID courseUuid)
    {
        var result = new HashMap<String, Object>();
        courseService.deleteCourse(courseUuid);
        result.put("success", true);
        return result;
    }

    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    @Autowired
    public void setCourseService(CourseService courseService)
    {
        this.courseService = courseService;
    }
}
