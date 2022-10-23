package farmconnect.farmconnectbackend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateDealDto {
    private Long id;
    private LocalDateTime dealDate;
    private LocalDateTime endDate;
    private String crop;
    private Integer priceMin;
    private Integer priceMax;
    private Integer quantityMinKg;
    private Integer quantityMaxKg;
}