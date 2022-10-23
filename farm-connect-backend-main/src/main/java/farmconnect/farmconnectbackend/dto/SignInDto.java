package farmconnect.farmconnectbackend.dto;

import lombok.Getter;

@Getter
public class SignInDto {
    // 검증 조건 추가하기
    private String uid;
    private String password;
}
