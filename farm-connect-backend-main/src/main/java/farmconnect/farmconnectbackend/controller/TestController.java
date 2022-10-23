package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.FcmSendDto;
import farmconnect.farmconnectbackend.repository.deal.DealRepositorySupport;
import farmconnect.farmconnectbackend.repository.user.UserRepository;
import farmconnect.farmconnectbackend.service.user.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final UserRepository userRepository;
    private final FcmService firebaseCloudMessageService;
    private final DealRepositorySupport dealRepositorySupport;

    // fcm send test
    @PostMapping("/fcm/send")
    public ResponseEntity pushMessage(@RequestBody FcmSendDto fcmSendDto) throws IOException {
        firebaseCloudMessageService.sendMessageTo(
                fcmSendDto.getTargetToken(),
                fcmSendDto.getTitle(),
                fcmSendDto.getBody());
        return ResponseEntity.ok().build();
    }

    // JWT 인증 요청 테스트
    @GetMapping("/jwtToken")
    public ResponseEntity test(@RequestAttribute String userUid) {
        return ResponseEntity.ok().body("토큰 인증 완료 및 데이터 불러오기 성공 "+userRepository.findByUid(userUid).get().getUid()+"님");
    }

    // JWT 인증 요청 테스트
    @GetMapping("/end")
    public ResponseEntity end(@RequestAttribute String userUid) {
        dealRepositorySupport.setAllIsEndByEndDate();
        return ResponseEntity.ok().body("토큰 인증 완료 및 데이터 불러오기 성공 "+userRepository.findByUid(userUid).get().getUid()+"님");
    }
}
