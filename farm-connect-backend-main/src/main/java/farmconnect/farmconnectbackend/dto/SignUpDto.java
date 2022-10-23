package farmconnect.farmconnectbackend.dto;

import lombok.Getter;

@Getter
public class SignUpDto {
    // TODO :: 검증 조건 추가하기
    private String uid;
    private String password;
    private String address;
    private String addressDetail;
    private Double latitude;
    private Double longitude;
    private String phone;
    private Boolean isFarmer;

    private String option1;
    private String option2;
    private String optionImageName;
}
