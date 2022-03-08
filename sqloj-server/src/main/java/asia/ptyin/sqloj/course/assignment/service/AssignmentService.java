package asia.ptyin.sqloj.course.assignment.service;

import asia.ptyin.sqloj.course.assignment.AssignmentDto;
import asia.ptyin.sqloj.course.assignment.AssignmentEntity;

import java.util.List;
import java.util.UUID;

public interface AssignmentService
{
    List<AssignmentEntity> getAssignmentListByCourse(UUID courseUuid);
    AssignmentEntity findAssignment(UUID assignmentUuid);
    AssignmentEntity openAssignment(AssignmentDto assignmentDto);
    AssignmentEntity updateAssignment(AssignmentEntity entity, AssignmentDto assignmentDto);

    void deleteAssignment(UUID assignmentUuid);
}
