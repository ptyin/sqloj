package asia.ptyin.sqloj.user;

import asia.ptyin.sqloj.course.CourseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/***
 * JPA entity class for user.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "plt_user")
@Getter @Setter
public class UserEntity
{
    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(unique = true)
    private String username;
    private String password;

    private boolean enabled = true;

    private UserRole role;

    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

    @OneToMany(
            mappedBy = "createdBy",
            cascade = CascadeType.ALL,
            targetEntity = UserEntity.class,
            fetch = FetchType.LAZY
    )
    private List<UserEntity> createdUserList;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            optional = false
    )
    private UserEntity createdBy;

    @ManyToMany
    @JoinTable(name = "plt_r_participates")
    private List<CourseEntity> participatedCourseList;

    public static UserEntity createUser(String username, String password, UserEntity creator)
    {
        var user = new UserEntity();
        user.username = username;
        user.password = password;

        user.createdBy = creator;
        user.setRole(UserRole.STUDENT);

        return user;
    }

    public static UserEntity createDefaultAdmin(String username, String password)
    {
        var admin = new UserEntity();
        admin.username = username;
        admin.password = password;

        admin.createdBy = admin;
        admin.setRole(UserRole.TEACHER);
        return admin;
    }
}
