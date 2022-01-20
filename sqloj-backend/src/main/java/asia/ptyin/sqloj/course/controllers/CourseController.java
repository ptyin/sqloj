package asia.ptyin.sqloj.course.controllers;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.service.CourseService;
import asia.ptyin.sqloj.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RequestMapping("/course")
@RestController
public class CourseController
{

    private UserService userService;
    private CourseService courseService;

    /**
     * Open a course.
     */
    @PostMapping
    public Map<String, Object> open(@Valid @RequestBody CourseDto courseDto, @RequestBody List<UUID> participatorUuidList)
    {
        var result = new HashMap<String, Object>();
        courseService.openCourse(courseDto, userService.findAllUser(participatorUuidList));

        result.put("success", true);
        return result;
    }

    @PutMapping
    public Map<String, Object> update()
    {
        return null;
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
