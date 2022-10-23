package farmconnect.farmconnectbackend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MainStatResponseDto {
    private int distributorCnt;
    private int farmerCnt;
    private long totalQuantityKg;
    private long totalPrice;

    @Builder
    public MainStatResponseDto(int distributorCnt, int farmerCnt, long totalQuantityKg, long totalPrice) {
        this.distributorCnt = distributorCnt;
        this.farmerCnt = farmerCnt;
        this.totalQuantityKg = totalQuantityKg;
        this.totalPrice = totalPrice;
    }
}
