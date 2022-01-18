package asia.ptyin.sqloj.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Calendar;
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
public class User
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
            targetEntity = User.class,
            fetch = FetchType.LAZY
    )
    private List<User> createdUserList;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            optional = false
    )
    private User createdBy;

    public static User registerUser(String username, String password, User creator)
    {
        var user = new User();
        user.username = username;
        user.password = password;

        user.createdBy = creator;
        user.setRole(UserRole.STUDENT);

        return user;
    }

    public static User createDefaultAdmin(String username, String password)
    {
        var admin = new User();
        admin.username = username;
        admin.password = password;

        admin.createdBy = admin;
        admin.setRole(UserRole.TEACHER);
        return admin;
    }
}
