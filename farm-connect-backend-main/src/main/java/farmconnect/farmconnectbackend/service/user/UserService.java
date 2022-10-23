package farmconnect.farmconnectbackend.service.user;

import farmconnect.farmconnectbackend.dto.SignInDto;
import farmconnect.farmconnectbackend.dto.SignUpDto;
import farmconnect.farmconnectbackend.dto.response.GetNameResponseDto;
import farmconnect.farmconnectbackend.dto.response.MyPageResponseDto;
import farmconnect.farmconnectbackend.entity.user.*;
import farmconnect.farmconnectbackend.filter.CustomAuthenticationEntryPoint;
import farmconnect.farmconnectbackend.repository.comment.CommentRepositorySupport;
import farmconnect.farmconnectbackend.repository.deal.DealRepositorySupport;
import farmconnect.farmconnectbackend.repository.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final UserWaitingRepository userWaitingRepository;
    private final UserRepository userRepository;
    private final UserDistributorDetailRepository userDistributorDetailRepository;
    private final UserFarmerDetailRepository userFarmerDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final FcmTokenRepository fcmTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmService fcmService;

    private final TokenRepositorySupport tokenRepositorySupport;
    private final CommentRepositorySupport commentRepositorySupport;
    private final DealRepositorySupport dealRepositorySupport;

    public Long signUp(SignUpDto user) {
        Long id = userWaitingRepository.save(
                UserWaiting.builder()
                        .uid(user.getUid())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .address(user.getAddress())
                        .addressDetail(user.getAddressDetail())
                        .latitude(user.getLatitude())
                        .longitude(user.getLongitude())
                        .phone(user.getPhone())
                        .isFarmer(user.getIsFarmer())
                        .option1(user.getOption1())
                        .option2(user.getOption2())
                        .optionImageName(user.getOptionImageName())
                        .build())
                .getId();
        return id;
    }

    public List<UserWaiting> getUserWaitingList() {
        return userWaitingRepository.findAll();
    }

    public Boolean checkUnique(String uid) {
        Boolean result = userRepository.existsByUid(uid);
        if (!result) result = userWaitingRepository.existsByUid(uid);

        return !result;
    }

    public Long permitUser(Long waitingId, Boolean isPermit) {
        UserWaiting user = userWaitingRepository.findById(waitingId).get();

        // 승인하면
        if (isPermit) {
            // 회원 저장
            Long userId = userRepository.save(
                            User.builder()
                                    .uid(user.getUid())
                                    .password(user.getPassword())
                                    .address(user.getAddress())
                                    .addressDetail(user.getAddressDetail())
                                    .latitude(user.getLatitude())
                                    .longitude(user.getLongitude())
                                    .phone(user.getPhone())
                                    .isFarmer(user.getIsFarmer())
                                    .roles(Collections.singletonList("ROLE_USER"))
                                    .build())
                    .getId();

            // 옵션 정보 저장
            User savedUser = userRepository.findById(userId).get();

            // 농민이라면
            if (user.getIsFarmer()) {
                userFarmerDetailRepository.save(
                        UserFarmerDetail.builder()
                                .user(savedUser)
                                .farmerName(user.getOption1())
                                .cropHandling(user.getOption2())
                                .farmerImage(user.getOptionImageName())
                                .build()
                );
            } else {
                userDistributorDetailRepository.save(
                        UserDistributorDetail.builder()
                                .user(savedUser)
                                .distributorName(user.getOption1())
                                .businessRegistrationNum(user.getOption2())
                                .businessRegistrationImage(user.getOptionImageName())
                                .build()
                );
            }

            userWaitingRepository.deleteById(waitingId);

            // 메시지 만들기
            try {
                fcmService.sendMessageTo(user.getFcmToken(), "팜커넥트", "회원가입 승인이 완료되었습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return userId;
        }

        // 승인 거절하면
        else {
            userWaitingRepository.deleteById(waitingId);
            return 0L;
        }
    }

    public User findUserByUid(String user) {
        User member = userRepository.findByUid(user).get();
        return member;
    }

    public Boolean signIn (String refreshToken, String fcmToken, User user) {

        refreshTokenRepository.save(new RefreshToken(refreshToken));
        if (fcmTokenRepository.existsByFcmToken(fcmToken)) fcmTokenRepository.deleteByFcmToken(fcmToken);
        fcmTokenRepository.save(new FcmToken(fcmToken, user, refreshToken));

        logger.info(user.getUid() + " (id : " + user.getId() + ") login");
        return true;
    }

    public Boolean signOut (String refreshToken, String fcmToken, User user) {
        if ( !fcmTokenRepository.existsByFcmToken(fcmToken) || !refreshTokenRepository.existsByRefreshToken(refreshToken)) return false;

        fcmTokenRepository.deleteByFcmToken(fcmToken);
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        logger.info(user.getUid() + " (id : " + user.getId() + ") logout");
        return true;
    }

    public Boolean deleteUser(User user) {
        dealRepositorySupport.deleteClosedDealByUserId(user.getId());
        commentRepositorySupport.deleteNewCommentByUserId(user.getId());
        commentRepositorySupport.deleteCommentByUserId(user.getId());
        dealRepositorySupport.deleteDealByUserId(user.getId());

        tokenRepositorySupport.deleteDeletedUserRefreshToken(user.getId());
        tokenRepositorySupport.deleteDeletedUserFcmToken(user.getId());

        userDistributorDetailRepository.deleteByUserId(user.getId());
        userFarmerDetailRepository.deleteByUserId(user.getId());
        userRepository.deleteById(user.getId());

        return true;
    }

    public boolean checkPassword(User member, SignInDto user) {
        return passwordEncoder.matches(user.getPassword(), member.getPassword());
    }

    public MyPageResponseDto getMyPageInfo(String userUid) {
        User user = userRepository.findByUid(userUid).get();
        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(user);
        if (myPageResponseDto.getUser().getIsFarmer()) {
            myPageResponseDto.setFarmerName(userFarmerDetailRepository.findById(user.getId()).get().getFarmerName());
            myPageResponseDto.setCropHandling(userFarmerDetailRepository.findById(user.getId()).get().getCropHandling());
        } else {
            myPageResponseDto.setDistributorName(userDistributorDetailRepository.findById(user.getId()).get().getDistributorName());
            myPageResponseDto.setBusinessRegistrationNum(userDistributorDetailRepository.findById(user.getId()).get().getBusinessRegistrationNum());
        }
        return myPageResponseDto;
    }

    public GetNameResponseDto getName(String userUid) {
        GetNameResponseDto result = new GetNameResponseDto();
        User user = userRepository.findByUid(userUid).get();
        if (user.getIsFarmer()) {
            result.setIsFarmer(true);
            result.setName(userFarmerDetailRepository.findById(user.getId()).get().getFarmerName());
            return result;
        }
        else {
            result.setIsFarmer(false);
            result.setName(userDistributorDetailRepository.findById(user.getId()).get().getDistributorName());
            return result;
        }
    }
}
