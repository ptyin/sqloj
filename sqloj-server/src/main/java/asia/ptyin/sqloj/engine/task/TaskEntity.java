package asia.ptyin.sqloj.engine.task;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/***
 * Entity class for task.
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "eng_task")
@Getter @Setter
public class TaskEntity
{
    @Id
    @GeneratedValue
    private UUID uuid;

    @CreatedDate
    private Date createdAt;
}
