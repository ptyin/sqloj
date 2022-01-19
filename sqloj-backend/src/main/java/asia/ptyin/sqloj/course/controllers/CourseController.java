package asia.ptyin.sqloj.course.controllers;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RequestMapping("/course")
@RestController
public class CourseController
{

    private CourseService courseService;

    /**
     * Open a course.
     */
    @PostMapping
    public Map<String, Object> open(@Valid @RequestBody CourseDto courseDto)
    {
        var result = new HashMap<String, Object>();
        courseService.openCourse(courseDto);

        result.put("success", true);
        return result;
    }

    @PutMapping
    public Map<String, Object> update()
    {
        return null;
    }

    @Autowired
    public void setCourseService(CourseService courseService)
    {
        this.courseService = courseService;
    }
}
