package asia.ptyin.sqloj.course;

import asia.ptyin.sqloj.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
    private UUID uuid;

    @Column(nullable = false)
    private String name;
    private String description;

    private boolean enabled = true;

    @GeneratedValue
    private Date createdAt;

    @ManyToMany(mappedBy = "participatedCourseList")
    private List<UserEntity> participatorList;

}
