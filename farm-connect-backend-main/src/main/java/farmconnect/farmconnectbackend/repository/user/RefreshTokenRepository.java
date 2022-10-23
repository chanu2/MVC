package farmconnect.farmconnectbackend.repository.user;

import farmconnect.farmconnectbackend.entity.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Ref;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByRefreshToken(String token);

    RefreshToken findByRefreshToken(String token);

    @Transactional
    void deleteByRefreshToken(String token);
}

