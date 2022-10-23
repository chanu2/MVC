package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.CropItemDto;
import farmconnect.farmconnectbackend.dto.UpdateDealDto;
import farmconnect.farmconnectbackend.dto.response.CurrDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.ListDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.MainStatResponseDto;
import farmconnect.farmconnectbackend.entity.user.UserWaiting;
import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.service.AdminService;
import farmconnect.farmconnectbackend.service.DealService;
import farmconnect.farmconnectbackend.service.StatService;
import farmconnect.farmconnectbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guest")
public class GuestController {
    private final UserService userService;
    private final DealService dealService;
    private final StatService statService;

    @GetMapping("/deal/list/filter")
    public ResponseEntity getDealListWithOption(@RequestParam("keyword") Optional<String> keywordParam,
                                                @RequestParam("type") Optional<String> typeParam,
                                                @RequestParam("crop") Optional<String> cropParam,
                                                @RequestParam("area") Optional<String> areaParam) throws IOException {
        String keyword = keywordParam.orElse("");
        String crop = cropParam.orElse("");
        String type = typeParam.orElse("");
        String area = areaParam.orElse("");

        List<ListDealResponseDto> list = dealService.getFilterDealList(keyword, crop, type, area);

        return list.size() > 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 조회 완료", list), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 거래 없음", new ArrayList()), HttpStatus.OK);
    }

    @GetMapping("/deal/id/{dealId}")
    public ResponseEntity getCurrentDeal(@RequestAttribute("userUid") String userUid, @PathVariable("dealId") Long dealId) throws IOException {
        CurrDealResponseDto currDealResponseDto = dealService.getCurrentDeal(dealId, userUid);

        return currDealResponseDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 거래 조회 완료", currDealResponseDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 거래 없음", currDealResponseDto), HttpStatus.OK);
    }

    @GetMapping("/crop/list")
    public ResponseEntity getCropItemByKeyword(@RequestParam("keyword") Optional<String> keywordParam) throws IOException {
        String keyword = keywordParam.orElse("");
        List<String> crops = dealService.getCropItemList(keyword);

        return crops.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "관련 작물 조회 완료", crops), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 작물 없음", new ArrayList()), HttpStatus.OK);
    }

    @GetMapping("/stat/main")
    public ResponseEntity getMainStat() throws IOException {
        MainStatResponseDto mainStatResponse = statService.getMainStat();

        return mainStatResponse != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "메인 통계 반환 완료", mainStatResponse), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "통계 없음"), HttpStatus.OK);
    }

    @GetMapping("/stat/crop/avg")
    public ResponseEntity getCropAvg() throws IOException {
        List<CropItemDto> crops = statService.getCropItemAvg();

        return crops != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "작물 평균 통계 반환 완료", crops), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "통계 없음"), HttpStatus.OK);
    }
}
