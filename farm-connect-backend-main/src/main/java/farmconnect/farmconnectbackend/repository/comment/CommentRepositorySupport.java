package farmconnect.farmconnectbackend.repository.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import farmconnect.farmconnectbackend.dto.response.NewCommentResponseDto;
import farmconnect.farmconnectbackend.entity.*;
import farmconnect.farmconnectbackend.entity.user.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Comment.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<NewCommentResponseDto> findAllNewComments(User currUser) {
        QNewComment c = QNewComment.newComment;
        QUserDistributorDetail ud = QUserDistributorDetail.userDistributorDetail;
        QUserFarmerDetail uf = QUserFarmerDetail.userFarmerDetail;

        return jpaQueryFactory.select(Projections.bean(NewCommentResponseDto.class, c.id,
                        c.comment, ud.distributorName, ud.businessRegistrationNum, uf.farmerName, uf.cropHandling))
                .from(c)
                .leftJoin(ud).on(c.comment.user.id.eq(ud.id))
                .leftJoin(uf).on(c.comment.user.id.eq(uf.id))
                .where(c.targetUser.eq(currUser))
                .fetch();
    }

    public List<NewCommentResponseDto> findAllNewCommentsByDeal(User currUser, Deal deal) {
        QNewComment c = QNewComment.newComment;
        QDeal d = QDeal.deal;
        QUserDistributorDetail ud = QUserDistributorDetail.userDistributorDetail;
        QUserFarmerDetail uf = QUserFarmerDetail.userFarmerDetail;

        return jpaQueryFactory.select(Projections.bean(NewCommentResponseDto.class, c.id,
                        c.comment, ud.distributorName, ud.businessRegistrationNum, uf.farmerName, uf.cropHandling))
                .from(c)
                .leftJoin(ud).on(c.comment.user.id.eq(ud.id))
                .leftJoin(uf).on(c.comment.user.id.eq(uf.id))
                .where(c.targetUser.eq(currUser).and(c.originDeal.eq(deal)))
                .fetch();
    }

    @Transactional
    public long deleteCommentByCrop(String crop) {

        QDeal d = QDeal.deal;
        CropItem item = new CropItem(crop);
        QComment c = QComment.comment;
        QClosedDeal cd = QClosedDeal.closedDeal;

        List<ClosedDeal> list = jpaQueryFactory
                .select(cd)
                .from(cd)
                .fetch();

        Long num = jpaQueryFactory
                .delete(c)
                .where(c.deal.in(
                        JPAExpressions
                                .select(d)
                                .from(d)
                                .where(d.crop.eq(item))))
                .execute();
        return num;
    }

    @Transactional
    public long deleteNewCommentByCrop(String crop) {
        QNewComment c = QNewComment.newComment;
        QDeal d = QDeal.deal;
        CropItem item = new CropItem(crop);

        return jpaQueryFactory
                .delete(c)
                .where(c.originDeal.in(
                        JPAExpressions
                                .select(d)
                                .from(d)
                                .where(d.crop.eq(item))))
                .execute();
    }

    @Transactional
    public long deleteNewCommentByDealAndUser(Deal deal, User currUser) {
        QNewComment c = QNewComment.newComment;

        return jpaQueryFactory
                .delete(c)
                .where(c.targetUser.eq(currUser).and(c.originDeal.eq(deal)))
                .execute();
    }

    @Transactional
    public long deleteNewCommentByUserId(Long userId) {
        QNewComment c = QNewComment.newComment;
        QComment cm = QComment.comment;
        QDeal d = QDeal.deal;

        return jpaQueryFactory
                .delete(c)
                .where(c.originDeal.id.eqAny(jpaQueryFactory.select(d.id).from(d).where(d.user.id.eq(userId)))
                        .or(c.targetUser.id.eq(userId))
                        .or(c.comment.id.eqAny(jpaQueryFactory.select(cm.id).from(cm).where(cm.user.id.eq(userId)))))
                .execute();
    }

    @Transactional
    public long deleteCommentByUserId(Long userId) {
        QComment c = QComment.comment;
        QDeal d = QDeal.deal;


        return jpaQueryFactory
                .delete(c)
                .where(c.deal.id.eqAny(jpaQueryFactory.select(d.id).from(d).where(d.user.id.eq(userId)))
                        .or(c.user.id.eq(userId)))
                .execute();
    }
}
