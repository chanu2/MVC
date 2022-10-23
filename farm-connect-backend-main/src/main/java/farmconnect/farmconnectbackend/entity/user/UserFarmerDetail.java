package farmconnect.farmconnectbackend.entity.user;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFarmerDetail {
    @Id
    @Column
    private Long id;

    @MapsId
    @OneToOne
    private User user;

    @Column(nullable = false, length = 200)
    private String farmerName;

    @Column(nullable = false, length = 200)
    private String cropHandling;

    @Column(length = 300)
    private String farmerImage;

    @Builder
    public UserFarmerDetail(Long id, User user, String farmerName, String cropHandling, String farmerImage) {
        this.id = id;
        this.user = user;
        this.farmerName = farmerName;
        this.cropHandling = cropHandling;
        this.farmerImage = farmerImage;
    }
}
