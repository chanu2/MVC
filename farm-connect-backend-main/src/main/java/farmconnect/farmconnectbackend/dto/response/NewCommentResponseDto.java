package farmconnect.farmconnectbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import farmconnect.farmconnectbackend.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCommentResponseDto {
    Long id;
    Comment comment;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String distributorName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String businessRegistrationNum = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String farmerName = "";

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String cropHandling = "";

    @QueryProjection
    public NewCommentResponseDto(Comment comment, String distributorName, String businessRegistrationNum, String farmerName, String cropHandling) {
        this.comment = comment;
        this.distributorName = distributorName;
        this.businessRegistrationNum = businessRegistrationNum;
        this.farmerName = farmerName;
        this.cropHandling = cropHandling;
    }

    public NewCommentResponseDto() {
    }
}
