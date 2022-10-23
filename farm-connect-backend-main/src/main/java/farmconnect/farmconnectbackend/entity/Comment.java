package farmconnect.farmconnectbackend.entity;

import farmconnect.farmconnectbackend.entity.user.User;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Deal deal;

    @Column(nullable = false)
    private Integer suggestPrice;

    @Column(nullable = false)
    private Integer suggestQuantity;

    @Column(nullable = false)
    private Double distance;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder

    public Comment(User user, Deal deal, Integer suggestPrice, Integer suggestQuantity, Double distance) {
        this.user = user;
        this.deal = deal;
        this.suggestPrice = suggestPrice;
        this.suggestQuantity = suggestQuantity;
        this.distance = distance;
    }
}
