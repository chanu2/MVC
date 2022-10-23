package farmconnect.farmconnectbackend.repository.user;
import farmconnect.farmconnectbackend.entity.user.UserFarmerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserFarmerDetailRepository extends JpaRepository<UserFarmerDetail, Long> {
    UserFarmerDetail save(UserFarmerDetail userFarmerDetail);
    Optional<UserFarmerDetail> findById(Long id);

    @Transactional
    void deleteByUserId(Long id);
}
