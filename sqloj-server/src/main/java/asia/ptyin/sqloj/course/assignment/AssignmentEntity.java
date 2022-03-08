package asia.ptyin.sqloj.course.assignment;


import asia.ptyin.sqloj.course.CourseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "plt_assignment")
@Getter @Setter
public class AssignmentEntity
{
    @Id
    @GeneratedValue
    UUID uuid;

    private String name;

    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    private Date startedAt;
    @Column(nullable = false)
    private Date endedAt;

    @ManyToOne(
            targetEntity = CourseEntity.class,
            optional = false
    )
    private CourseEntity course;
}
