package asia.ptyin.sqloj.course;

import asia.ptyin.sqloj.course.assignment.AssignmentEntity;
import asia.ptyin.sqloj.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "plt_course")
@Getter @Setter
public class CourseEntity
{
    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    private Date startedAt;
    @Column(nullable = false)
    private Date endedAt;

    private boolean enabled = true;

    @CreatedBy
    private UUID createdBy;

    @CreatedDate
    private Date createdAt;

    @ManyToMany
    @JoinTable(name = "plt_r_participates")
    private List<UserEntity> participatorList;

    @OneToMany(
            targetEntity = AssignmentEntity.class,
            cascade = CascadeType.ALL,
            mappedBy = "course",
            fetch = FetchType.LAZY
    )
    private List<AssignmentEntity> assignmentList;
}
