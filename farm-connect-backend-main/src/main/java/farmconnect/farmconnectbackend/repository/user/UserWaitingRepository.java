package farmconnect.farmconnectbackend.repository.user;

import farmconnect.farmconnectbackend.entity.user.User;
import farmconnect.farmconnectbackend.entity.user.UserWaiting;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserWaitingRepository extends JpaRepository<UserWaiting, Long> {
    UserWaiting save(UserWaiting userWaiting);
    Optional<UserWaiting> findById(Long id);
    Optional<UserWaiting> findByUid(String uid);

    @Transactional
    void deleteById(Long id);

    List<UserWaiting> findAll();

    boolean existsByUid(String uid);
}
