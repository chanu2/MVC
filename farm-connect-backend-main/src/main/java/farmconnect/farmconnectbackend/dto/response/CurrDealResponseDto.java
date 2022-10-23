package farmconnect.farmconnectbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import farmconnect.farmconnectbackend.entity.ClosedDeal;
import farmconnect.farmconnectbackend.entity.Comment;
import farmconnect.farmconnectbackend.entity.Deal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrDealResponseDto {
    private Deal deal;
    private List comments;
    private Integer commentCnt;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String distributorName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String businessRegistrationNum = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String farmerName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String cropHandling = "";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long closedComment;
}