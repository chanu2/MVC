package farmconnect.farmconnectbackend.entity.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDistributorDetail {
    @Id
    @Column
    private Long id;

    @MapsId
    @OneToOne
    private User user;

    @Column(nullable = false, length = 200)
    private String distributorName;

    @Column(nullable = false, length = 200)
    private String businessRegistrationNum;

    @Column(length = 300)
    private String businessRegistrationImage;

    @Builder
    public UserDistributorDetail(Long id, User user, String distributorName, String businessRegistrationNum, String businessRegistrationImage) {
        this.id = id;
        this.user = user;
        this.distributorName = distributorName;
        this.businessRegistrationNum = businessRegistrationNum;
        this.businessRegistrationImage = businessRegistrationImage;
    }
}
