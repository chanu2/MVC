package farmconnect.farmconnectbackend.dto;

import com.querydsl.core.annotations.QueryProjection;
import farmconnect.farmconnectbackend.entity.CropItem;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CropItemDto {
    private String cropItem;
    private Integer itemAvg;

    public CropItemDto() {
    }

    @QueryProjection
    public CropItemDto(String cropItem, Integer itemAvg) {
        this.cropItem = cropItem;
        this.itemAvg = itemAvg;
    }
}
