package farmconnect.farmconnectbackend.dto;

import lombok.Getter;

@Getter
public class AddCommentDto {
    private Long dealId;
    private Integer suggestPrice;
    private Integer suggestQuantity;
}