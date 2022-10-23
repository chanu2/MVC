package farmconnect.farmconnectbackend.repository.user;

import farmconnect.farmconnectbackend.entity.user.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    boolean existsByFcmToken(String token);
    List<FcmToken> findAllByUserId(Long userId);
    FcmToken findByRefreshToken(String refreshToken);

    @Transactional
    @Modifying
    @Query("UPDATE FcmToken f SET f.refreshToken = :newRefreshToken WHERE f.refreshToken = :oldRefreshToken")
    Integer updateRefreshToken(String oldRefreshToken, String newRefreshToken);

    @Transactional
    @Modifying
    @Query("UPDATE FcmToken f SET f.updatedAt = :updatedAt WHERE f.refreshToken = :newRefreshToken")
    Integer updateUpdateAt(String newRefreshToken, LocalDateTime updatedAt);

    @Transactional
    void deleteByFcmToken(String token);
}
