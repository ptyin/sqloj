package asia.ptyin.sqloj.course;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "plt_course")
@Getter @Setter
public class Course
{
    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String name;
    private String description;

    @GeneratedValue
    private Date createdAt;

}
