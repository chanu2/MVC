package farmconnect.farmconnectbackend.dto.response;

import farmconnect.farmconnectbackend.entity.Deal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetNameResponseDto {
    private Boolean isFarmer;
    private String name;
}
