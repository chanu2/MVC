package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.response.GetNameResponseDto;
import farmconnect.farmconnectbackend.dto.response.MyPageResponseDto;
import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.config.JwtTokenProvider;
import farmconnect.farmconnectbackend.dto.SignInDto;
import farmconnect.farmconnectbackend.dto.SignUpDto;
import farmconnect.farmconnectbackend.entity.user.FcmToken;
import farmconnect.farmconnectbackend.entity.user.RefreshToken;
import farmconnect.farmconnectbackend.entity.user.User;
import farmconnect.farmconnectbackend.entity.user.UserWaiting;
import farmconnect.farmconnectbackend.repository.user.FcmTokenRepository;
import farmconnect.farmconnectbackend.repository.user.RefreshTokenRepository;
import farmconnect.farmconnectbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.password}")
    private String adminPassword;

    // 아이디 중복체크
    @PostMapping("/checkUnique")
    public ResponseEntity check(@RequestBody HashMap<String, String> param) {
        String uid = param.get("uid");
        Boolean result = userService.checkUnique(uid);

        return result ?
        new ResponseEntity(DefaultRes.res(StatusCode.OK, "사용가능한 아이디입니다"), HttpStatus.OK):
        new ResponseEntity(DefaultRes.res(StatusCode.OK, "중복된 아이디입니다"), HttpStatus.OK);
    }

    // 회원가입 요청
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody SignUpDto user) {
        Long result = userService.signUp(user);
        
        return result != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "회원가입 요청을 성공하였습니다"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestHeader("FcmToken") String fcmToken, @RequestBody SignInDto user, HttpServletResponse response) {
            User member = userService.findUserByUid(user.getUid());
            if (member == null) return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "없는 사용자"), HttpStatus.OK);

            Boolean isRight = userService.checkPassword(member, user);
            if (!isRight) return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 비밀번호"), HttpStatus.OK);

        // 어세스, 리프레시 토큰 발급 및 헤더 설정
        String accessToken = jwtTokenProvider.createAccessToken(member.getUid(), member.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUid(), member.getRoles());

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        // 리프레시 토큰 저장소에 저장
        userService.signIn(refreshToken, fcmToken, member);

        if (user.getUid().equals("admin")) return new ResponseEntity(DefaultRes.res(StatusCode.OK, "관리자 로그인 완료", "ROLE_ADMIN"), HttpStatus.OK);
        else if (user.getUid().equals("guest")) return new ResponseEntity(DefaultRes.res(StatusCode.OK, "게스트 로그인 완료", "ROLE_GUEST"), HttpStatus.OK);
        else return new ResponseEntity(DefaultRes.res(StatusCode.OK, "사용자 로그인 완료", "ROLE_USER"), HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping("/signOut")
    public ResponseEntity signOut(@RequestHeader("RefreshToken") String refreshToken, @RequestHeader("FcmToken") String fcmToken, @RequestAttribute String userUid) {
        refreshToken = refreshToken.substring(7);
        User member = userService.findUserByUid(userUid);
        Boolean existAndOut = userService.signOut(refreshToken, fcmToken, member);

        return existAndOut ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "로그아웃 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @GetMapping("/myPage/info")
    public ResponseEntity myPageInfo(@RequestAttribute String userUid) {
        MyPageResponseDto myPageResponseDto = userService.getMyPageInfo(userUid);

        return myPageResponseDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "마이페이지 정보 조회 완료", myPageResponseDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @GetMapping("/getName")
    public ResponseEntity getName(@RequestAttribute String userUid) {
        GetNameResponseDto userName = userService.getName(userUid);

        return userName != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "사업자명/농장주명 반환 완료", userName), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    // 회원 삭제
    @PostMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestAttribute String userUid) {
        User member = userService.findUserByUid(userUid);
        Boolean existAndOut = userService.deleteUser(member);

        return existAndOut ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "계정 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    // 통합 예외 핸들러
    @ExceptionHandler
    public String exceptionHandler(Exception exception) {
        return exception.getMessage();
    }
}
