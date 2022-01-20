package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    UserEntity testUser1, testUser2, testUser3;
    List<UUID> testUserList;
    CourseEntity testCourse;

    @BeforeEach
    void setUp()
    {

        testUser1 = userService.registerUser(new UserDto("course-service-test1", "course-service-test@123"), userService.findUser("admin"));
        testUser2 = userService.registerUser(new UserDto("course-service-test2", "course-service-test@123"), userService.findUser("admin"));
        testUser3 = userService.registerUser(new UserDto("course-service-test3", "course-service-test@123"), userService.findUser("admin"));

        testUserList = new ArrayList<>();
        testUserList.add(testUser1.getUuid());
        testUserList.add(testUser2.getUuid());
        testUserList.add(testUser3.getUuid());
    }

    @AfterEach
    void tearDown()
    {
        if(testCourse != null)
            courseService.deleteCourse(testCourse);
        userService.deleteUser(testUser1);
        userService.deleteUser(testUser2);
        userService.deleteUser(testUser3);
    }

    @Test
    void openCourse()
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        testCourse = courseService.openCourse(courseDto, userService.findAllUser(testUserList));
        assertNotNull(testCourse.getCreatedAt(), "Created date not null");
        assertEquals(testCourse.getParticipatorList().size(), testUserList.size());
        assertEquals(courseService.getUserParticipatedCourseList(testUser1.getUuid()).get(0).getName(), "2021-2022数据库原理1班");
    }

    @Test
    void getParticipatorList()
    {
        // Open a test course.
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        testCourse = courseService.openCourse(courseDto, userService.findAllUser(testUserList));

        var participatorList = courseService.getParticipatorList(testCourse.getUuid());
        assertEquals(participatorList.size(), testUserList.size());
        for(int i = 0; i < testUserList.size(); i++)
            assertEquals(participatorList.get(i).getUuid(), testUserList.get(i));
    }
}