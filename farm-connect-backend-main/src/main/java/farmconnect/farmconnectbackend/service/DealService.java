package farmconnect.farmconnectbackend.service;

import farmconnect.farmconnectbackend.dto.AddDealDto;
import farmconnect.farmconnectbackend.dto.CloseDealDto;
import farmconnect.farmconnectbackend.dto.UpdateDealDto;
import farmconnect.farmconnectbackend.dto.response.CurrDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.ListDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.ListMyDealResponseDto;
import farmconnect.farmconnectbackend.entity.*;
import farmconnect.farmconnectbackend.entity.user.User;
import farmconnect.farmconnectbackend.repository.comment.CommentRepository;
import farmconnect.farmconnectbackend.repository.comment.CommentRepositorySupport;
import farmconnect.farmconnectbackend.repository.comment.NewCommentRepository;
import farmconnect.farmconnectbackend.repository.deal.ClosedDealRepository;
import farmconnect.farmconnectbackend.repository.deal.CropItemRepository;
import farmconnect.farmconnectbackend.repository.deal.DealRepository;
import farmconnect.farmconnectbackend.repository.deal.DealRepositorySupport;
import farmconnect.farmconnectbackend.repository.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DealService {
    private final UserRepository userRepository;
    private final CropItemRepository cropItemRepository;
    private final DealRepository dealRepository;
    private final UserDistributorDetailRepository userDistributorDetailRepository;
    private final UserFarmerDetailRepository userFarmerDetailRepository;
    private final CommentRepository commentRepository;
    private final ClosedDealRepository closedDealRepository;
    private final CommentService commentService;
    private final NewCommentRepository newCommentRepository;
    private final DealRepositorySupport dealRepositorySupport;
    private final CommentRepositorySupport commentRepositorySupport;

    public Long addDeal(AddDealDto addDealDto, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        if (addUser.getIsFarmer() && addDealDto.getIsTypeSell()) return null;
        if (!addUser.getIsFarmer() && !addDealDto.getIsTypeSell()) return null;
        if (addDealDto.getPriceMin() > addDealDto.getPriceMax() || addDealDto.getQuantityMinKg() > addDealDto.getQuantityMaxKg()) return null;

        // 공백제거
        String cropStr = addDealDto.getCrop().replace(" ","");

        // 있는 작물인지 확인
        if (!cropItemRepository.existsByCropItem(cropStr)) {
            cropItemRepository.save(new CropItem(cropStr));
        }

        // 없으면 추가하기
        CropItem currCrop = cropItemRepository.findByCropItem(cropStr);

        Long dealId = dealRepository.save(
                Deal.builder()
                        .user(addUser)
                        .isTypeSell(addDealDto.getIsTypeSell())
                        .isEnd(false)
                        .dealDate(addDealDto.getDealDate())
                        .endDate(addDealDto.getEndDate())
                        .crop(currCrop)
                        .priceMin(addDealDto.getPriceMin())
                        .priceMax(addDealDto.getPriceMax())
                        .quantityMinKg(addDealDto.getQuantityMinKg())
                        .quantityMaxKg(addDealDto.getQuantityMaxKg())
                        .build()
        ).getId();

        return dealId;
    }

    public Long updateDeal(UpdateDealDto updateDealDto, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        if (dealRepository.findById(updateDealDto.getId()).get().getUser() != addUser) return null;
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

    public Long deleteDeal(Long dealId, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        if (dealRepository.findById(dealId).get().getUser() != addUser) return null;

        closedDealRepository.deleteByDealId(dealId);
        newCommentRepository.deleteAllByOriginDealId(dealId);
        commentRepository.deleteAllByDealId(dealId);
        dealRepository.deleteById(dealId);

        return dealId;
    }

    public List<ListDealResponseDto> getAllDealList() {
        List<ListDealResponseDto> dealList = new ArrayList<>();
        List<Deal> originDeals = dealRepository.findAll();

        for (Deal d:originDeals) {

            // 농장주의 매입이면
            if (!d.getIsTypeSell()) {
                ListDealResponseDto listDealResponseDto = new ListDealResponseDto();
                listDealResponseDto.setDeal(d);
                listDealResponseDto.setCommentCnt(commentRepository.findAllByDealId(d.getId()).size());
                listDealResponseDto.setCropHandling(userFarmerDetailRepository.findById(d.getUser().getId()).get().getCropHandling());
                listDealResponseDto.setFarmerName(userFarmerDetailRepository.findById(d.getUser().getId()).get().getFarmerName());

                dealList.add(listDealResponseDto);
            }
            else {
                ListDealResponseDto listDealResponseDto = new ListDealResponseDto();
                listDealResponseDto.setDeal(d);
                listDealResponseDto.setCommentCnt(commentRepository.findAllByDealId(d.getId()).size());
                listDealResponseDto.setDistributorName(userDistributorDetailRepository.findById(d.getUser().getId()).get().getDistributorName());
                listDealResponseDto.setBusinessRegistrationNum(userDistributorDetailRepository.findById(d.getUser().getId()).get().getBusinessRegistrationNum());
            }
        }
        return dealList;
    }

    public boolean closeDeal(CloseDealDto closeDealDto, String userUid) {
        Deal currDeal = dealRepository.findById(closeDealDto.getDealId()).get();
        Comment currComment = closeDealDto.getCommentId() == -1?null : commentRepository.findById(closeDealDto.getCommentId()).get();

        if (!currDeal.getUser().getUid().equals(userUid)) return false;
        else if (closeDealDto.getCommentId() == -1) {
            dealRepository.updateIsEnd(currDeal.getId());

            return true;
        } else{
            dealRepository.updateIsEnd(currDeal.getId());
            closedDealRepository.save(new ClosedDeal(currDeal, currComment));

            return true;
        }
    }

    public CurrDealResponseDto getCurrentDeal(Long dealId, String userUid) {
        CurrDealResponseDto currDealResponseDto = new CurrDealResponseDto();

        Deal currDeal = dealRepository.findById(dealId).get();
        List<HashMap<String,Object>> currComments = commentService.getCommentByDeal(dealId);

        currDealResponseDto.setDeal(currDeal);
        currDealResponseDto.setComments(currComments);
        currDealResponseDto.setCommentCnt(currComments.size());

        User currDealUser = currDeal.getUser();
        if (currDealUser.getIsFarmer()) {
            currDealResponseDto.setFarmerName(userFarmerDetailRepository.findById(currDealUser.getId()).get().getFarmerName());
            currDealResponseDto.setCropHandling(userFarmerDetailRepository.findById(currDealUser.getId()).get().getCropHandling());
        } else {
            currDealResponseDto.setDistributorName(userDistributorDetailRepository.findById(currDealUser.getId()).get().getDistributorName());
            currDealResponseDto.setBusinessRegistrationNum(userDistributorDetailRepository.findById(currDealUser.getId()).get().getBusinessRegistrationNum());
        }

        // 성사된 거래라면 성사 정보 삽입
        if (currDeal.getIsEnd()) {
            // 만약 거래가 있다면 || 기간 지나서 마감된거면
            if (closedDealRepository.existsByDealId(dealId)) currDealResponseDto.setClosedComment(closedDealRepository.findByDealId(dealId).getComment().getId());
            else currDealResponseDto.setClosedComment((long) -1);
        } else {
            currDealResponseDto.setClosedComment((long) -1);
        }

        // 조회했음으로 새알림에서 삭제
        commentRepositorySupport.deleteNewCommentByDealAndUser(currDeal, userRepository.findByUid(userUid).get());

        return currDealResponseDto;
    }

    public List<String> getCropItemList(String keyword) {
        if (keyword.equals("")) return dealRepositorySupport.findAllCropItem();

        return dealRepositorySupport.findCropItemByKeyword(keyword);
    }

    public List<ListDealResponseDto> getFilterDealList(String keyword, String crop, String type, String area) {
        List<ListDealResponseDto> list = dealRepositorySupport.findByOption(keyword, crop, type, area);
        return list;
    }

    public List<ListMyDealResponseDto> getMyDealNotEndList (String userUid) {
        User currUser = userRepository.findByUid(userUid).get();
        List<ListMyDealResponseDto> deals = dealRepositorySupport.findAllByUserIdAndIsEnd(currUser,false);

        return deals;
    }

    public List<ListMyDealResponseDto> getMyDealEndList (String userUid) {
        User currUser = userRepository.findByUid(userUid).get();

        return dealRepositorySupport.findAllByUserIdAndIsEnd(currUser,true);
    }
}