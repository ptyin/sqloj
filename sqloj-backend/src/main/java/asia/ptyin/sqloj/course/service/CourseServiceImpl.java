package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.course.CourseNotFoundEntity;
import asia.ptyin.sqloj.course.CourseRepository;
import asia.ptyin.sqloj.user.UserEntity;
import asia.ptyin.sqloj.user.security.service.AuthenticationService;
import asia.ptyin.sqloj.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService
{
    private CourseRepository repository;

    @Override
    public List<CourseEntity> getUserParticipatedCourseList(UUID userUuid)
    {
        return repository.findAllByParticipatorList_Uuid(userUuid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserEntity> getParticipatorList(UUID courseUuid)
    {
        var course = repository.findById(courseUuid).orElse(null);
        if(course == null)
            throw new CourseNotFoundEntity(courseUuid);
        var participatorList = course.getParticipatorList();
        //noinspection ResultOfMethodCallIgnored
        participatorList.size();  // Lazy-initialize field.
        return participatorList;
    }

    @Override
    public CourseEntity findCourse(UUID courseUuid)
    {
        return null;
    }

    @Override
    public CourseEntity openCourse(CourseDto courseDto, List<UserEntity> participatorList)
    {
        var entity = new CourseEntity();
        entity.setName(courseDto.getName());
        entity.setDescription(courseDto.getDescription());
        entity.setStartedAt(courseDto.getStartedAt());
        entity.setEndedAt(courseDto.getEndedAt());

//        var participatorList = userService.findAllUser(courseDto.getParticipatorList());
        entity.setParticipatorList(participatorList);
        repository.save(entity);

        return entity;
    }

    @Override
    public void saveCourse(CourseEntity course)
    {
        repository.save(course);
    }

    @Override
    public void deleteCourse(CourseEntity course)
    {
        repository.delete(course);
    }

    @Autowired
    public void setRepository(CourseRepository repository)
    {
        this.repository = repository;
    }
}
