package asia.ptyin.sqloj.course.assignment.service;

import asia.ptyin.sqloj.course.CourseRepository;
import asia.ptyin.sqloj.course.assignment.AssignmentDto;
import asia.ptyin.sqloj.course.assignment.AssignmentEntity;
import asia.ptyin.sqloj.course.assignment.AssignmentNotFoundException;
import asia.ptyin.sqloj.course.assignment.AssignmentRepository;
import asia.ptyin.sqloj.course.service.CourseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class AssignmentServiceImpl implements AssignmentService
{

    @Setter(onMethod_ = @Autowired)
    private AssignmentRepository assignmentRepository;
    @Setter(onMethod_ = @Autowired)
    private CourseService courseService;

    @Override
    public List<AssignmentEntity> getAssignmentListByCourse(UUID courseUuid)
    {
        return assignmentRepository.findAllByCourse_Uuid(courseUuid);
    }

    @Override
    public AssignmentEntity findAssignment(UUID assignmentUuid)
    {
        var assignment = assignmentRepository.findById(assignmentUuid).orElse(null);
        if (assignment == null)
            throw new AssignmentNotFoundException(assignmentUuid);
        return assignment;
    }

    @Override
    public AssignmentEntity openAssignment(AssignmentDto assignmentDto)
    {
        var entity = new AssignmentEntity();
        entity.setName(assignmentDto.getName());
        entity.setStartedAt(assignmentDto.getStartedAt());
        entity.setEndedAt(assignmentDto.getEndedAt());
        entity.setCourse(courseService.findCourse(assignmentDto.getCourseUuid()));
        return assignmentRepository.save(entity);
    }

    @Override
    public AssignmentEntity updateAssignment(AssignmentEntity entity, AssignmentDto assignmentDto)
    {
        return null;
    }

    @Override
    public void deleteAssignment(UUID assignmentUuid)
    {

    }
}
