package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceTest
{

    @Autowired
    UserService userService;

    @Autowired
    CourseService courseService;

    @Test
    void openCourse()
    {
        var testUser1 = userService.registerUser(new UserDto("course-service-test1", "course-service-test@123"), userService.findUser("admin"));
        var testUser2 = userService.registerUser(new UserDto("course-service-test2", "course-service-test@123"), userService.findUser("admin"));
        var testUser3 = userService.registerUser(new UserDto("course-service-test3", "course-service-test@123"), userService.findUser("admin"));

        var testUserList = new ArrayList<UUID>();
        testUserList.add(testUser1.getUuid());
        testUserList.add(testUser2.getUuid());
        testUserList.add(testUser3.getUuid());

        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        var course = courseService.openCourse(courseDto);
        assertNotNull(course.getCreatedAt(), "Created date not null");
        assertEquals(course.getParticipatorList().size(), testUserList.size());
//        assertEquals(userService.findUser(testUser1.getUuid()).getParticipatedCourseList().get(0).getName(), "2021-2022数据库原理1班");

        var courseUuid = course.getUuid();
        courseService.deleteCourse(course);
        assertNull(courseService.findCourse(courseUuid));

        userService.deleteUser(testUser1);
        userService.deleteUser(testUser2);
        userService.deleteUser(testUser3);
    }
}