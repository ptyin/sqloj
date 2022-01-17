package asia.ptyin.sqloj.user.entities;

import asia.ptyin.sqloj.user.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/***
 * JPA entity class for user.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Entity
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

    @Temporal(value = TemporalType.DATE)
    private Calendar createdAt;


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
