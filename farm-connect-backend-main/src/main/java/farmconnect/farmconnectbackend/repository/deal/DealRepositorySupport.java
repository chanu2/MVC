package farmconnect.farmconnectbackend.repository.deal;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import farmconnect.farmconnectbackend.dto.UpdateDealDto;
import farmconnect.farmconnectbackend.dto.response.ListDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.ListMyDealResponseDto;
import farmconnect.farmconnectbackend.entity.*;
import farmconnect.farmconnectbackend.entity.user.QUserDistributorDetail;
import farmconnect.farmconnectbackend.entity.user.QUserFarmerDetail;
import farmconnect.farmconnectbackend.entity.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DealRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public DealRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Deal.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public Long updateDeal(UpdateDealDto updateDealDto) {
        QDeal d = QDeal.deal;

        return jpaQueryFactory
                .update(d)
                .set(d.dealDate, Expressions.asDateTime(updateDealDto.getDealDate()))
                .set(d.endDate, Expressions.asDateTime(updateDealDto.getEndDate()))
                .set(d.crop, new CropItem(updateDealDto.getCrop()))
                .set(d.priceMin, updateDealDto.getPriceMin())
                .set(d.priceMax, updateDealDto.getPriceMax())
                .set(d.quantityMinKg, updateDealDto.getQuantityMinKg())
                .set(d.quantityMaxKg, updateDealDto.getQuantityMaxKg())
                .where(d.id.eq(updateDealDto.getId()))
                .execute();
    }

    public List<ListMyDealResponseDto> findAllByUserIdAndIsEnd(User currUser, Boolean isEnd) {
        QDeal d = QDeal.deal;
        QSubQuery s = QSubQuery.subQuery;
        QNewCommentSubQuery ncs = QNewCommentSubQuery.newCommentSubQuery;
        QUserDistributorDetail ud = QUserDistributorDetail.userDistributorDetail;
        QUserFarmerDetail uf = QUserFarmerDetail.userFarmerDetail;

        return jpaQueryFactory.select(Projections.bean(ListMyDealResponseDto.class,
                        d, s.commentCnt, ud.distributorName, ud.businessRegistrationNum, uf.farmerName, uf.cropHandling, ncs.newCommentCnt))
                .from(d)
                .leftJoin(ud).on(d.user.id.eq(ud.id))
                .leftJoin(uf).on(d.user.id.eq(uf.id))
                .join(s).on(d.id.eq(s.dealId))
                .join(ncs).on(d.id.eq(ncs.dealId))
                .where(d.user.eq(currUser).and(d.isEnd.eq(isEnd)))
                .orderBy(d.endDate.asc())
                .fetch();
    }

    public List<ListDealResponseDto> findByOption(String keyword, String crop, String type, String area) {
        QDeal d = QDeal.deal;
        QSubQuery s = QSubQuery.subQuery;
        QUserDistributorDetail ud = QUserDistributorDetail.userDistributorDetail;
        QUserFarmerDetail uf = QUserFarmerDetail.userFarmerDetail;

        BooleanBuilder builder = new BooleanBuilder();
        if (!keyword.equals("")) {
            builder.and(d.crop.cropItem.contains(keyword)).or(d.user.uid.contains(keyword))
                    .or(ud.distributorName.contains(keyword))
                    .or(uf.farmerName.contains(keyword));
        }
        if (!crop.equals("")) {
            builder.and(d.crop.cropItem.eq(crop));
        }
        if (!type.equals("")) {
            if (type.equals("sell")) builder.and(d.isTypeSell.eq(true));
            else if (type.equals("buy")) builder.and(d.isTypeSell.eq(false));
        }
        if (!area.equals("")) {
            builder.and(d.user.address.substring(0, 2).eq(area));
        }

        return jpaQueryFactory.select(Projections.bean(ListDealResponseDto.class,
                        d, s.commentCnt, ud.distributorName, ud.businessRegistrationNum, uf.farmerName, uf.cropHandling))
                .from(d)
                .leftJoin(ud).on(d.user.id.eq(ud.id))
                .leftJoin(uf).on(d.user.id.eq(uf.id))
                .join(s).on(d.id.eq(s.dealId))
                .where(builder)
                //.orderBy(d.isEnd.asc(), d.endDate.asc())
                .orderBy(d.isEnd.asc(), d.createdAt.asc())
                .fetch();

    }

    @Transactional
    public long deleteClosedDealByUserId(Long userId) {
        QClosedDeal cd = QClosedDeal.closedDeal;
        QDeal d = QDeal.deal;
        QComment c = QComment.comment;

        return jpaQueryFactory
                .delete(cd)
                .where(cd.deal.id.eqAny(jpaQueryFactory.select(d.id).from(d).where(d.user.id.eq(userId)))
                        .or(cd.comment.id.eqAny(jpaQueryFactory.select(c.id).from(c).where(c.user.id.eq(userId)))))
                .execute();
    }

    @Transactional
    public long deleteDealByUserId(Long userId) {
        QDeal d = QDeal.deal;

        return jpaQueryFactory
                .delete(d)
                .where(d.user.id.eq(userId))
                .execute();
    }

    @Transactional
    public long deleteClosedDealByCrop(String crop) {
        QClosedDeal cd = QClosedDeal.closedDeal;
        QDeal d = QDeal.deal;
        CropItem item = new CropItem(crop);

        return jpaQueryFactory
                .delete(cd)
                .where(cd.deal.in(
                        JPAExpressions
                                .select(d)
                                .from(d)
                                .where(d.crop.eq(item))))
                .execute();
    }

    @Transactional
    public long deleteDealByCrop(String crop) {
        QDeal d = QDeal.deal;
        CropItem item = new CropItem(crop);

        return jpaQueryFactory
                .delete(d)
                .where(d.crop.eq(new CropItem(crop)))
                .execute();
    }

    @Transactional
    public void setAllIsEndByEndDate() {
        QDeal d = QDeal.deal;

        jpaQueryFactory
                .update(d)
                .set(d.isEnd, true)
                .where(d.endDate.before(Expressions.asDateTime(LocalDateTime.now())))
                .execute();
    }

    public List<String> findCropItemByKeyword(String keyword) {
        QCropItem c = QCropItem.cropItem1;

        return jpaQueryFactory.select(c.cropItem)
                .from(c)
                .where(c.cropItem.contains(keyword))
                .fetch();
    }

    public List<String> findAllCropItem() {
        QCropItem c = QCropItem.cropItem1;

        return jpaQueryFactory.select(c.cropItem)
                .from(c)
                .fetch();
    }
}
