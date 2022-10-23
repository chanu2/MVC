package farmconnect.farmconnectbackend.repository.user;

import farmconnect.farmconnectbackend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);

    @Transactional
    void deleteById(Long id);
}