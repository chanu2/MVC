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
public class UserWaiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length =50, unique = true)
    private String uid;

    @Column(nullable = false, length =100)
    private String password;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 200)
    private String addressDetail;

    @Column(nullable = false, length = 200)
    private Double latitude;

    @Column(nullable = false, length = 200)
    private Double longitude;

    @Column(nullable = false, length = 200)
    private String phone;

    @Column(nullable = false)
    private Boolean isFarmer;

    @Column(nullable = false, length = 200)
    private String option1;

    @Column(nullable = false, length = 200)
    private String option2;

    @Column(length = 300)
    private String optionImageName;

    @Column(length = 300)
    private String fcmToken;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public UserWaiting(String uid, String password, String address, String addressDetail, Double latitude, Double longitude, String phone,
                       Boolean isFarmer, String option1, String option2, String optionImageName, String fcmToken) {
        this.uid = uid;
        this.password = password;
        this.address = address;
        this.addressDetail = addressDetail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.isFarmer = isFarmer;
        this.option1 = option1;
        this.option2 = option2;
        this.optionImageName = optionImageName;
        this.fcmToken = fcmToken;
    }
}
