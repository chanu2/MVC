package farmconnect.farmconnectbackend.entity;

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
public class CropItem {
    @Id
    @Column(length = 50)
    private String cropItem;

    @Builder
    public CropItem(String crop) {
        this.cropItem = crop;
    }
}
