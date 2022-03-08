package asia.ptyin.sqloj.course.service;

import asia.ptyin.sqloj.course.CourseDto;
import asia.ptyin.sqloj.course.CourseEntity;
import asia.ptyin.sqloj.course.CourseNotFoundException;
import asia.ptyin.sqloj.course.CourseRepository;
import asia.ptyin.sqloj.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<UserEntity> getParticipatorList(UUID courseUuid)
    {
        var course = findCourseEagerly(courseUuid);
        return course.getParticipatorList();
    }

    @Override
    public CourseEntity findCourse(UUID courseUuid) throws CourseNotFoundException
    {
        var course = repository.findById(courseUuid).orElse(null);
        if(course == null)
            throw new CourseNotFoundException(courseUuid);
        return course;
    }

    @Override
    public CourseEntity findCourseEagerly(UUID courseUuid) throws CourseNotFoundException
    {
        var course = repository.findByUuidAndFetchEagerly(courseUuid);
        if(course == null)
            throw new CourseNotFoundException(courseUuid);
        return course;
    }

    private void initCourseEntity(CourseEntity entity, CourseDto courseDto, List<UserEntity> participatorList)
    {
        entity.setName(courseDto.getName());
        entity.setDescription(courseDto.getDescription());
        entity.setStartedAt(courseDto.getStartedAt());
        entity.setEndedAt(courseDto.getEndedAt());

//        var participatorList = userService.findAllUser(courseDto.getParticipatorList());
        entity.setParticipatorList(participatorList);
    }

    @Override
    public CourseEntity openCourse(CourseDto courseDto, List<UserEntity> participatorList)
    {
        var entity = new CourseEntity();
        initCourseEntity(entity, courseDto, participatorList);
        repository.save(entity);

        return entity;
    }

    @Override
    public CourseEntity updateCourse(CourseEntity entity, CourseDto courseDto, List<UserEntity> participatorList)
    {
        initCourseEntity(entity, courseDto, participatorList);
        repository.save(entity);
        return null;
    }

    @Override
    public void deleteCourse(CourseEntity course)
    {
        repository.delete(course);
    }

    @Override
    public void deleteCourse(UUID courseUuid)
    {
        repository.deleteById(courseUuid);
    }

    @Autowired
    public void setRepository(CourseRepository repository)
    {
        this.repository = repository;
    }
}
