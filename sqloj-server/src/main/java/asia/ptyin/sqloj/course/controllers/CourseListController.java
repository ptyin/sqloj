package asia.ptyin.sqloj.course.controllers;

import asia.ptyin.sqloj.course.service.CourseService;
import asia.ptyin.sqloj.user.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/course-list")
@RestController
public class CourseListController
{
    private AuthenticationService authenticationService;
    private CourseService courseService;

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    @GetMapping
    public List<?> getCourseList(Authentication authentication)
    {
        var userUuid = authenticationService.getUserUuid(authentication);
        return courseService.getUserParticipatedCourseList(userUuid);
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setCourseService(CourseService courseService)
    {
        this.courseService = courseService;
    }

}
