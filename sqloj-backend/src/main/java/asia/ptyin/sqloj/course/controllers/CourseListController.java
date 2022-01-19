package asia.ptyin.sqloj.course.controllers;

import asia.ptyin.sqloj.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/course-list")
@RestController
public class CourseListController
{
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService)
    {
        this.courseService = courseService;
    }

    @GetMapping
    public List<?> getCourseList(Authentication authentication)
    {
        return courseService.getCurrentUserCourseList(authentication);
    }
}
