package farmconnect.farmconnectbackend.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import farmconnect.farmconnectbackend.dto.CropItemDto;
import farmconnect.farmconnectbackend.dto.response.MainStatResponseDto;
import farmconnect.farmconnectbackend.entity.Deal;
import farmconnect.farmconnectbackend.entity.QDeal;
import farmconnect.farmconnectbackend.repository.deal.CropItemRepository;
import farmconnect.farmconnectbackend.repository.deal.DealRepository;
import farmconnect.farmconnectbackend.repository.user.UserDistributorDetailRepository;
import farmconnect.farmconnectbackend.repository.user.UserFarmerDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatService {
    private final CropItemRepository cropItemRepository;
    private final DealRepository dealRepository;
    private final UserDistributorDetailRepository userDistributorDetailRepository;
    private final UserFarmerDetailRepository userFarmerDetailRepository;

    private final JPAQueryFactory jpaQueryFactory;

    public MainStatResponseDto getMainStat() {
        long totalQuantityKg = 0;
        long totalPrice = 0;

        List<Deal> deals = dealRepository.findAll();
        for (Deal d:deals) {
            totalPrice += d.getPriceMax();
            totalQuantityKg += d.getQuantityMaxKg();
        }

        return MainStatResponseDto.builder()
                .distributorCnt(userDistributorDetailRepository.findAll().size())
                .farmerCnt(userFarmerDetailRepository.findAll().size())
                .totalQuantityKg(totalQuantityKg)
                .totalPrice(totalPrice).build();
    }

    public List<CropItemDto> getCropItemAvg() {
        List<CropItemDto> crops = findAllCropAvg();

        return crops;
    }

    public List<CropItemDto> findAllCropAvg(){
        QDeal d = QDeal.deal;

        return jpaQueryFactory.select(
                                Projections.bean(
                                        CropItemDto.class,
                                                d.crop.cropItem,
                                                d.priceMax.divide(2).add(d.priceMin.divide(2)).avg().intValue().as("itemAvg")
                                ))
                .from(d)
                .groupBy(d.crop)
                .limit(10)
                .fetch();
    }
}
