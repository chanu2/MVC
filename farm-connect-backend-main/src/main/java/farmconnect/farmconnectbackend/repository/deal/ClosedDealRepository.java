package farmconnect.farmconnectbackend.repository.deal;

import farmconnect.farmconnectbackend.entity.ClosedDeal;
import farmconnect.farmconnectbackend.entity.CropItem;
import farmconnect.farmconnectbackend.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ClosedDealRepository extends JpaRepository<ClosedDeal, Long> {
    ClosedDeal save(ClosedDeal closedDeal);
    ClosedDeal findByDealId(Long dealId);
    Boolean existsByDealId(Long dealId);

    @Transactional
    void deleteByDealId(Long dealId);
}