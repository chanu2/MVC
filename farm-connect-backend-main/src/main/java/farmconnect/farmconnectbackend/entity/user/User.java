package farmconnect.farmconnectbackend.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EnableJpaAuditing
@SpringBootApplication
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length =50, unique = true)
    private String uid;

    @JsonIgnore
    @Column(nullable = false, length =100)
    private String password;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 200)
    private String addressDetail;

    @JsonIgnore
    @Column(nullable = false, length = 200)
    private Double latitude;

    @JsonIgnore
    @Column(nullable = false, length = 200)
    private Double longitude;

    @Column(nullable = false, length = 200)
    private String phone;

    @Column(nullable = false, length = 200)
    private Boolean isFarmer;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @JsonIgnore
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(String uid, String password, String address, String addressDetail, Double latitude, Double longitude, String phone, Boolean isFarmer, List<String> roles) {
        this.uid = uid;
        this.password = password;
        this.address = address;
        this.addressDetail = addressDetail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.isFarmer = isFarmer;
        this.roles = roles;
    }
}
