package asia.ptyin.sqloj.course.controllers;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.course.CourseNotFoundException;
import asia.ptyin.sqloj.course.service.CourseService;
import asia.ptyin.sqloj.user.UserDto;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest
{

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;

    UserEntity testUser1, testUser2, testUser3;
    List<UUID> testUserList;
    CourseEntity testCourse;

    @BeforeEach
    void setUp()
    {
        testUser1 = userService.registerUser(new UserDto("course-controller-test1", "course-service-test@123"), userService.findUser("admin"));
        testUser2 = userService.registerUser(new UserDto("course-controller-test2", "course-service-test@123"), userService.findUser("admin"));
        testUser3 = userService.registerUser(new UserDto("course-controller-test3", "course-service-test@123"), userService.findUser("admin"));

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

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    @Test
    void getCourseList() throws Exception
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        testCourse = courseService.openCourse(courseDto, userService.findAllUser(testUserList));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var resultCourseDtoList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<CourseDto>>(){});
        assertEquals(resultCourseDtoList.size(), 0);
    }

    @WithMockUser(authorities = "STUDENT")
    @Test
    void get() throws Exception
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        testCourse = courseService.openCourse(courseDto, userService.findAllUser(testUserList));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseUuid}", testCourse.getUuid()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var resultCourseDto = mapper.readValue(result.getResponse().getContentAsString(), CourseDto.class);
        assertEquals(testCourse.getName(), resultCourseDto.getName());
        assertEquals(testUserList.size(), resultCourseDto.getParticipatorList().size());
    }

    @WithMockUser(authorities = "TEACHER")
    @Test
    void open() throws Exception
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        var body = mapper.writeValueAsString(courseDto);
        mockMvc.perform(post("/courses").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        testCourse = courseService.getUserParticipatedCourseList(testUser1.getUuid()).get(0);
        assertEquals(testCourse.getName(), "2021-2022数据库原理1班");
    }

    @WithMockUser(username = "course-controller-test1", authorities = "STUDENT")
    @Test
    void openAsStudent() throws Exception
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        var body = mapper.writeValueAsString(courseDto);

        mockMvc.perform(post("/courses").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "TEACHER")
    @Test
    void update() throws Exception
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        testCourse = courseService.openCourse(courseDto, userService.findAllUser(testUserList));
        assertEquals(testCourse.getName(), "2021-2022数据库原理1班");
        var updatedCourseDto = new CourseDto("2019-2020数据库原理1班", "", new Date(), new Date(), testUserList);
        var body = mapper.writeValueAsString(updatedCourseDto);
        mockMvc.perform(put("/courses/{courseUuid}", testCourse.getUuid()).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        testCourse = courseService.findCourse(testCourse.getUuid());
        assertEquals(testCourse.getName(), "2019-2020数据库原理1班");
    }

    @WithMockUser(authorities = "TEACHER")
    @Test
    void delete() throws Exception
    {
        var courseDto = new CourseDto("2021-2022数据库原理1班", "", new Date(), new Date(), testUserList);
        testCourse = courseService.openCourse(courseDto, userService.findAllUser(testUserList));
        assertNotNull(courseService.findCourse(testCourse.getUuid()));
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/{courseUuid}", testCourse.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        assertThrows(CourseNotFoundException.class, () -> courseService.findCourse(testCourse.getUuid()));
        testCourse = null;
    }
}