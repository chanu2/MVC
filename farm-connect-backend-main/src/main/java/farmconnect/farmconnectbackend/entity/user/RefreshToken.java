package farmconnect.farmconnectbackend.entity.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EnableJpaAuditing
@SpringBootApplication
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {
    @Id
    @Column(nullable = false, length = 191)
    private String refreshToken;

    @CreatedDate
    @Column(nullable = false)
    LocalDateTime createdAt;

    @Builder
    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
