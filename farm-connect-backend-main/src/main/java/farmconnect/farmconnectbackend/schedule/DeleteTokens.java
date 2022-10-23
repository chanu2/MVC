package farmconnect.farmconnectbackend.schedule;

import farmconnect.farmconnectbackend.filter.CustomAuthenticationEntryPoint;
import farmconnect.farmconnectbackend.repository.deal.DealRepositorySupport;
import farmconnect.farmconnectbackend.repository.user.TokenRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class DeleteTokens {
    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final TokenRepositorySupport tokenRepositorySupport;
    private final DealRepositorySupport dealRepositorySupport;

    // 자정마다 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInvalidTokens() {
        // 만료된 토큰 삭제
        tokenRepositorySupport.deleteExpiredRefreshToken();
        tokenRepositorySupport.deleteExpiredFcmToken();

        // 만료된 거래 갱신
        dealRepositorySupport.setAllIsEndByEndDate();

        logger.info("Deleted expired tokens");



    }
}
