package farmconnect.farmconnectbackend.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
public class FcmToken {
    @Id
    @Column(nullable = false, length = 191)
    private String fcmToken;

    @ManyToOne
    private User user;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 191)
    private String refreshToken;

    @UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public FcmToken(String fcmToken, User user, String refreshToken) {
        this.fcmToken = fcmToken;
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
