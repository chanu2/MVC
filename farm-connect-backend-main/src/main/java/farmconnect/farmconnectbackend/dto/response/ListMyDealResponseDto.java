package farmconnect.farmconnectbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import farmconnect.farmconnectbackend.entity.Deal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListMyDealResponseDto {
    private Deal deal;
    private Integer commentCnt;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String distributorName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String businessRegistrationNum = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String farmerName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String cropHandling = "";

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer newCommentCnt;

    public ListMyDealResponseDto() {
    }

    // 클라이언트 요청으로 수정
    private String optionName;

    private String optionDetail;

    @QueryProjection
    public ListMyDealResponseDto(Deal deal, Integer commentCnt, String distributorName, String businessRegistrationNum, String farmerName, String cropHandling, Integer newCommentCnt) {
        this.deal = deal;
        this.commentCnt = commentCnt;
        this.distributorName = distributorName;
        this.businessRegistrationNum = businessRegistrationNum;
        this.farmerName = farmerName;
        this.cropHandling = cropHandling;
        this.newCommentCnt = newCommentCnt;
    }
}
