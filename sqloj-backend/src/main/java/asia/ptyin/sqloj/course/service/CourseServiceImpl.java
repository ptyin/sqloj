package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.course.CourseRepository;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.security.service.AuthenticationService;
import asia.ptyin.sqloj.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService
{
    private AuthenticationService authenticationService;
    private UserService userService;
    private CourseRepository repository;

    @Override
    public List<CourseEntity> getCurrentUserCourseList(Authentication authentication)
    {
        var userUuid = authenticationService.getUserUuid(authentication);
        return repository.findAllByParticipatorList_Uuid(userUuid);
    }

    @Override
    public CourseEntity findCourse(UUID uuid)
    {
        return null;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
    public CourseEntity openCourse(CourseDto courseDto)
    {
        var entity = new CourseEntity();
        entity.setName(courseDto.getName());
        entity.setDescription(courseDto.getDescription());
        entity.setStartedAt(courseDto.getStartedAt());
        entity.setEndedAt(courseDto.getEndedAt());

        var participatorList = new ArrayList<UserEntity>();
        for(var uuid : courseDto.getParticipatorList())
        {
            var participator = userService.findUser(uuid);
            participatorList.add(participator);
        }
        entity.setParticipatorList(participatorList);
        repository.save(entity);
        System.out.println("-----------------" + userService.findUser(courseDto.getParticipatorList().get(0)).getParticipatedCourseList().get(0).getName());

        return entity;
    }

    @Override
    public void deleteCourse(CourseEntity course)
    {
        repository.delete(course);
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    @Autowired
    public void setRepository(CourseRepository repository)
    {
        this.repository = repository;
    }
}
