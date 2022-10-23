package farmconnect.farmconnectbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import farmconnect.farmconnectbackend.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyPageResponseDto {
    private User user;
    // 클라의 요청으로 필드 포함
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String distributorName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String businessRegistrationNum = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String farmerName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String cropHandling = "";

    @Builder
    public MyPageResponseDto(User user) {
        this.user = user;
    }


}