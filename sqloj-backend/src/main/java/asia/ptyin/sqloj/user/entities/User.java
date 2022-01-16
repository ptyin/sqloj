package asia.ptyin.sqloj.user.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/***
 * JPA entity class for user.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Entity
@Table(name = "plt_user")
@Data
@NoArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private boolean admin;
    private boolean enabled = true;

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

    public static User createDefaultAdmin(String username, String password)
    {
        var admin = new User();
        admin.username = username;
        admin.password = password;

        admin.admin = true;
        admin.createdBy = admin;
        return admin;
    }
}
