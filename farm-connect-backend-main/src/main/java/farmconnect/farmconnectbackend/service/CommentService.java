package farmconnect.farmconnectbackend.service;

import farmconnect.farmconnectbackend.dto.AddCommentDto;
import farmconnect.farmconnectbackend.dto.response.NewCommentResponseDto;
import farmconnect.farmconnectbackend.entity.*;
import farmconnect.farmconnectbackend.entity.user.*;
import farmconnect.farmconnectbackend.repository.comment.CommentRepository;
import farmconnect.farmconnectbackend.repository.comment.CommentRepositorySupport;
import farmconnect.farmconnectbackend.repository.deal.DealRepository;
import farmconnect.farmconnectbackend.repository.comment.NewCommentRepository;
import farmconnect.farmconnectbackend.repository.user.FcmTokenRepository;
import farmconnect.farmconnectbackend.repository.user.UserDistributorDetailRepository;
import farmconnect.farmconnectbackend.repository.user.UserFarmerDetailRepository;
import farmconnect.farmconnectbackend.repository.user.UserRepository;
import farmconnect.farmconnectbackend.service.user.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final UserDistributorDetailRepository userDistributorDetailRepository;
    private final UserFarmerDetailRepository userFarmerDetailRepository;
    private final DealRepository dealRepository;
    private final CommentRepository commentRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final NewCommentRepository newCommentRepository;
    private final FcmService fcmService;
    private final CommentRepositorySupport commentRepositorySupport;

    public Long addComment(AddCommentDto addCommentDto, String userUid) throws IOException {
        Deal currDeal = dealRepository.findById(addCommentDto.getDealId()).get();
        User currCommentUser = userRepository.findByUid(userUid).get();

        if (currCommentUser.getIsFarmer() && !currDeal.getIsTypeSell()) return null;
        if (!currCommentUser.getIsFarmer() && currDeal.getIsTypeSell()) return null;
        if (currDeal.getIsEnd()) return null;

        String contentsName = "";
        if (currCommentUser.getIsFarmer()) {
            contentsName = userFarmerDetailRepository.findById(currCommentUser.getId()).get().getFarmerName();
        } else {
            contentsName = userDistributorDetailRepository.findById(currCommentUser.getId()).get().getDistributorName();
        }

        String isSellStr = currDeal.getIsTypeSell()?"구매":"판매";

        // 메시지 만들기
        String contents = "["+ contentsName + "] " + addCommentDto.getSuggestPrice().toString() + "원/1kg   " + addCommentDto.getSuggestQuantity().toString() + "kg";

        List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUserId(currDeal.getUser().getId());
        for (FcmToken f:fcmTokens) {
            fcmService.sendMessageTo(f.getFcmToken(),  "[" + isSellStr + "]" + currDeal.getCrop().getCropItem() + "새로운 댓글 알림", contents);
        }

        Comment comment = commentRepository.save(
                Comment.builder()
                        .user(currCommentUser)
                        .deal(currDeal)
                        .suggestPrice(addCommentDto.getSuggestPrice())
                        .suggestQuantity(addCommentDto.getSuggestQuantity())
                        .distance(getDistance(currDeal.getUser().getLatitude(), currDeal.getUser().getLongitude(), currCommentUser.getLatitude(), currCommentUser.getLongitude(), "km"))
                        .build()
        );

        // 댓글 알림 리스트를 위해 테이블에 따로 저장
        newCommentRepository.save(new NewComment(comment, comment.getDeal(), comment.getDeal().getUser()));

        return comment.getId();
    }

    public Long deleteComment(Long commentId, String userUid) throws IOException {
        User addUser = userRepository.findByUid(userUid).get();
        if (commentRepository.findById(commentId).get().getUser() != addUser) return null;
        if (commentRepository.findById(commentId).get().getDeal().getIsEnd()) return null;

        newCommentRepository.deleteAllByCommentId(commentId);
        commentRepository.deleteById(commentId);

        return commentId;
    }

    public List getCommentByDeal(Long dealId) {
        // 매도/매매에 따라 필드 변화가 있기 때문에 해시맵으로 생성
        List<HashMap<String, Object>> comments = new ArrayList<>();
        List<Comment> originComments = commentRepository.findAllByDealId(dealId);

        for (Comment c : originComments) {
            HashMap<String,Object> map = new LinkedHashMap<String,Object>();
            map.put("id", c.getId());
            map.put("user", c.getUser());

            if (c.getUser().getIsFarmer()) {
                UserFarmerDetail userFarmerDetail = userFarmerDetailRepository.findById(c.getUser().getId()).get();
                map.put("farmerName", userFarmerDetail.getFarmerName());
                map.put("cropHandling", userFarmerDetail.getCropHandling());
                map.put("distributorName", "");
                map.put("businessRegistrationNum", "");
            } else {
                UserDistributorDetail userDistributorDetail = userDistributorDetailRepository.findById(c.getUser().getId()).get();
                map.put("distributorName", userDistributorDetail.getDistributorName());
                map.put("businessRegistrationNum", userDistributorDetail.getBusinessRegistrationNum());
                map.put("farmerName", "");
                map.put("cropHandling", "");
            }

            map.put("dealId", c.getDeal().getId());
            map.put("suggestPrice", c.getSuggestPrice());
            map.put("suggestQuantity", c.getSuggestQuantity());
            map.put("distance", c.getDistance());
            map.put("createdAt", c.getCreatedAt());
            comments.add(map);
        }

        return comments;
    }

    public List<NewCommentResponseDto> getNewComments(String userUid) {
        User currUser = userRepository.findByUid(userUid).get();

        return commentRepositorySupport.findAllNewComments(currUser);
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "km") {
            dist = dist * 1.609344;
        } else if(unit == "m"){
            dist = dist * 1609.344;
        }

        return (dist);
    }

    // decimal degrees를 radians로
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // radians를 decimal degrees로
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
