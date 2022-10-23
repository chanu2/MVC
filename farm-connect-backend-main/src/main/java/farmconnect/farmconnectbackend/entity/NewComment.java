package farmconnect.farmconnectbackend.entity;

import farmconnect.farmconnectbackend.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EnableJpaAuditing
@SpringBootApplication
@EntityListeners(AuditingEntityListener.class)
public class NewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    Comment comment;

    @ManyToOne
    Deal originDeal;

    @ManyToOne
    User targetUser;

    @Builder
    public NewComment(Comment comment, Deal originDeal, User targetUser) {
        this.comment = comment;
        this.originDeal = originDeal;
        this.targetUser = targetUser;
    }
}
