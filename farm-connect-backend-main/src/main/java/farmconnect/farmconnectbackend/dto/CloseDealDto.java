package farmconnect.farmconnectbackend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CloseDealDto {
    private Long dealId;
    private Long commentId;
}
