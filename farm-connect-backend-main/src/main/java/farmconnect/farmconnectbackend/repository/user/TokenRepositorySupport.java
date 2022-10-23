package farmconnect.farmconnectbackend.repository.user;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import farmconnect.farmconnectbackend.entity.Deal;
import farmconnect.farmconnectbackend.entity.QNewComment;
import farmconnect.farmconnectbackend.entity.user.QFcmToken;
import farmconnect.farmconnectbackend.entity.user.QRefreshToken;
import farmconnect.farmconnectbackend.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Repository
public class TokenRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public TokenRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public long deleteExpiredRefreshToken() {
        QRefreshToken rt = QRefreshToken.refreshToken1;

        return jpaQueryFactory
                .delete(rt)
                .where(rt.createdAt.before(Expressions.asDateTime(LocalDateTime.now().minusDays(30))))
                .execute();
    }

    @Transactional
    public long deleteExpiredFcmToken() {
        QFcmToken ft = QFcmToken.fcmToken1;

        return jpaQueryFactory
                .delete(ft)
                .where(ft.updatedAt.before(Expressions.asDateTime(LocalDateTime.now().minusDays(30))))
                .execute();
    }

    @Transactional
    public long deleteDeletedUserRefreshToken(Long userId) {
        QRefreshToken rt = QRefreshToken.refreshToken1;
        QFcmToken ft = QFcmToken.fcmToken1;

        return jpaQueryFactory
                .delete(rt)
                .where(rt.refreshToken.eqAny(jpaQueryFactory.select(ft.refreshToken).from(ft).where(ft.user.id.eq(userId))))
                .execute();
    }

    @Transactional
    public long deleteDeletedUserFcmToken(Long userId) {
        QFcmToken ft = QFcmToken.fcmToken1;

        return jpaQueryFactory
                .delete(ft)
                .where(ft.user.id.eq(userId))
                .execute();
    }
}