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
    public CourseEntity getCourseByUuid(UUID uuid)
    {
        return null;
    }

    @Override
    public boolean openCourse(CourseDto courseDto)
    {
        var entity = new CourseEntity();
        entity.setName(courseDto.getName());
        entity.setDescription(courseDto.getDescription());
        entity.setStartedAt(courseDto.getStartedAt());
        entity.setEndedAt(courseDto.getEndedAt());

        var participatorList = new ArrayList<UserEntity>();
        for(var uuid : courseDto.getParticipatorList())
            participatorList.add(userService.findUser(uuid));
        entity.setParticipatorList(participatorList);

        repository.save(entity);
        return true;
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
