package farmconnect.farmconnectbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime dealDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isTypeSell;

    @Column(nullable = false, length =100)
    private Boolean isEnd;

    @ManyToOne
    @JoinColumn(name = "crop_item")
    private CropItem crop;

    @Column(nullable = false)
    private Integer priceMin;

    @Column(nullable = false)
    private Integer priceMax;

    @Column(nullable = false)
    private Integer quantityMinKg;

    @Column(nullable = false)
    private Integer quantityMaxKg;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Deal(User user, LocalDateTime dealDate, LocalDateTime endDate, Boolean isTypeSell,
                Boolean isEnd, CropItem crop, Integer priceMin, Integer priceMax, Integer quantityMinKg, Integer quantityMaxKg) {
        this.user = user;
        this.dealDate = dealDate;
        this.endDate = endDate;
        this.isTypeSell = isTypeSell;
        this.isEnd = isEnd;
        this.crop = crop;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.quantityMinKg = quantityMinKg;
        this.quantityMaxKg = quantityMaxKg;
    }
}
