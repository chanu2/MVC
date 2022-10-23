package farmconnect.farmconnectbackend.repository.user;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import farmconnect.farmconnectbackend.entity.user.QFcmToken;
import farmconnect.farmconnectbackend.entity.user.QRefreshToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public class UserRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
}