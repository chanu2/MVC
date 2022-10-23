package farmconnect.farmconnectbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import farmconnect.farmconnectbackend.entity.Deal;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Setter
@Getter
public class ListDealResponseDto {
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


    public ListDealResponseDto() {
    }

    @QueryProjection
    public ListDealResponseDto(Deal deal, Integer commentCnt, String distributorName1, String businessRegistrationNum1, String farmerName1, String cropHandling1) {
        this.deal = deal;
        this.commentCnt = commentCnt;
        this.distributorName = distributorName1;
        this.businessRegistrationNum = businessRegistrationNum1;
        this.farmerName = farmerName1;
        this.cropHandling = cropHandling1;
    }
}
