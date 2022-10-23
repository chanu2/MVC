package farmconnect.farmconnectbackend.dto;

import farmconnect.farmconnectbackend.entity.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddDealDto {
    private Boolean isTypeSell;
    private LocalDateTime dealDate;
    private LocalDateTime endDate;
    private String crop;
    private Integer priceMin;
    private Integer priceMax;
    private Integer quantityMinKg;
    private Integer quantityMaxKg;
}