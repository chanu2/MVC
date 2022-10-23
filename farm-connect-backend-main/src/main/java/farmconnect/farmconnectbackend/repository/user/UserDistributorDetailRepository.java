package farmconnect.farmconnectbackend.repository.user;

import farmconnect.farmconnectbackend.entity.user.UserDistributorDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserDistributorDetailRepository extends JpaRepository<UserDistributorDetail, Long> {
    UserDistributorDetail save(UserDistributorDetail userDistributorDetail);
    Optional<UserDistributorDetail> findById(Long id);

    @Transactional
    void deleteByUserId(Long id);
}