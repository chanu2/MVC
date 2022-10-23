package farmconnect.farmconnectbackend.dto;

import lombok.Getter;

@Getter
public class FcmSendDto {
    private String targetToken;
    private String title;
    private String body;
}
