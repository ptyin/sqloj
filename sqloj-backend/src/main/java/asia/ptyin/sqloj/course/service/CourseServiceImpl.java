package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.course.CourseRepository;
import asia.ptyin.sqloj.user.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService
{
    private AuthenticationService authenticationService;
    private CourseRepository repository;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

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
}
