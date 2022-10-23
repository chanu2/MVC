package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.CloseDealDto;
import farmconnect.farmconnectbackend.dto.UpdateDealDto;
import farmconnect.farmconnectbackend.dto.response.CurrDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.ListDealResponseDto;
import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.dto.AddDealDto;
import farmconnect.farmconnectbackend.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;

    @PostMapping("/add")
    public ResponseEntity addDeal(@RequestAttribute("userUid") String userUid, @RequestBody List<AddDealDto> deals) throws IOException {
        Long id = null;
        for (AddDealDto d:deals) {
            id = dealService.addDeal(d, userUid);
            if (id == null) break;
        }

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 추가 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity addDeal(@RequestAttribute("userUid") String userUid, @RequestBody UpdateDealDto updateDealDto) throws IOException {
        Long id = dealService.updateDeal(updateDealDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 수정 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity addDeal(@RequestAttribute("userUid") String userUid, @RequestBody HashMap<String, Long> param) throws IOException {
        Long id = dealService.deleteDeal(param.get("dealId"), userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @GetMapping("/list/all")
    public ResponseEntity getDealList() throws IOException {
        List deals = dealService.getAllDealList();

        return deals.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 조회 완료", deals), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 거래 없음", new ArrayList()), HttpStatus.OK);
    }

    @PostMapping("/close")
    public ResponseEntity closeDeal (@RequestAttribute("userUid") String userUid, @RequestBody CloseDealDto closeDealDto) throws IOException {
        Boolean closed = dealService.closeDeal(closeDealDto, userUid);

        return closed ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 성사 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @GetMapping("/id/{dealId}")
    public ResponseEntity getDealList(@RequestAttribute("userUid") String userUid, @PathVariable("dealId") Long dealId) throws IOException {
        CurrDealResponseDto currDealResponseDto = dealService.getCurrentDeal(dealId, userUid);

        return currDealResponseDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 거래 조회 완료", currDealResponseDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 거래 없음", currDealResponseDto), HttpStatus.OK);
    }

    @GetMapping("/list/filter")
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

    @GetMapping("/list/myPage/isNotEnd")
    public ResponseEntity getMyDealNotEndList(@RequestAttribute("userUid") String userUid) throws IOException {
        List deals = dealService.getMyDealNotEndList(userUid);

        return deals.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 조회 완료", deals), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 거래 없음", new ArrayList()), HttpStatus.OK);
    }

    @GetMapping("/list/myPage/isEnd")
    public ResponseEntity getMyDealEndList(@RequestAttribute("userUid") String userUid) throws IOException {
        List deals = dealService.getMyDealEndList(userUid);

        return deals.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 조회 완료", deals), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 거래 없음", new ArrayList()), HttpStatus.OK);
    }

    @GetMapping("/crop/list")
    public ResponseEntity getCropItemByKeyword(@RequestParam("keyword") Optional<String> keywordParam) throws IOException {
        String keyword = keywordParam.orElse("");
        List<String> crops = dealService.getCropItemList(keyword);

        return crops.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "관련 작물 조회 완료", crops), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 작물 없음", new ArrayList()), HttpStatus.OK);
    }
}

