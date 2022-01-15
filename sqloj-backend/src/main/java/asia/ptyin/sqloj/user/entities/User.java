package asia.ptyin.sqloj.user.entities;

import lombok.Data;

import javax.persistence.*;
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
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;
    private String username;
    private String password;

    private boolean isAdmin;

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
}
