package farmconnect.farmconnectbackend.service;

import farmconnect.farmconnectbackend.dto.UpdateDealDto;
import farmconnect.farmconnectbackend.entity.CropItem;
import farmconnect.farmconnectbackend.entity.user.User;
import farmconnect.farmconnectbackend.repository.comment.CommentRepository;
import farmconnect.farmconnectbackend.repository.comment.CommentRepositorySupport;
import farmconnect.farmconnectbackend.repository.comment.NewCommentRepository;
import farmconnect.farmconnectbackend.repository.deal.ClosedDealRepository;
import farmconnect.farmconnectbackend.repository.deal.CropItemRepository;
import farmconnect.farmconnectbackend.repository.deal.DealRepository;
import farmconnect.farmconnectbackend.repository.deal.DealRepositorySupport;
import farmconnect.farmconnectbackend.repository.user.UserDistributorDetailRepository;
import farmconnect.farmconnectbackend.repository.user.UserFarmerDetailRepository;
import farmconnect.farmconnectbackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CropItemRepository cropItemRepository;
    private final DealRepository dealRepository;
    private final UserDistributorDetailRepository userDistributorDetailRepository;
    private final UserFarmerDetailRepository userFarmerDetailRepository;
    private final CommentRepository commentRepository;
    private final NewCommentRepository newCommentRepository;
    private final ClosedDealRepository closedDealRepository;
    private final CommentService commentService;
    private final DealRepositorySupport dealRepositorySupport;
    private final CommentRepositorySupport commentRepositorySupport;

    public Long updateDeal(UpdateDealDto updateDealDto) {
        if (dealRepository.findById(updateDealDto.getId()).get().getIsEnd()) return null;

        // 공백제거
        String cropStr = updateDealDto.getCrop().replace(" ","");

        // 있는 작물인지 확인
        if (!cropItemRepository.existsByCropItem(cropStr)) {
            CropItem c = cropItemRepository.save(new CropItem(cropStr));
        }

        dealRepositorySupport.updateDeal(updateDealDto);

        return updateDealDto.getId();
    }

    public boolean deleteDeal(Long dealId) {
        // deal 삭제
        closedDealRepository.deleteByDealId(dealId);
        newCommentRepository.deleteAllByOriginDealId(dealId);
        commentRepository.deleteAllByDealId(dealId);
        dealRepository.deleteById(dealId);

        return true;
    }

    public boolean deleteCrop(String crop) {
        dealRepositorySupport.deleteClosedDealByCrop(crop);
        commentRepositorySupport.deleteNewCommentByCrop(crop);
        commentRepositorySupport.deleteCommentByCrop(crop);
        dealRepositorySupport.deleteDealByCrop(crop);
        cropItemRepository.deleteByCropItem(crop);

        return true;
    }
}
