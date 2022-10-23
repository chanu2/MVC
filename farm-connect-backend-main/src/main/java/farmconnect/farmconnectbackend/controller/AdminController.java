package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.UpdateDealDto;
import farmconnect.farmconnectbackend.dto.response.CurrDealResponseDto;
import farmconnect.farmconnectbackend.dto.response.ListDealResponseDto;
import farmconnect.farmconnectbackend.entity.user.UserWaiting;
import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.service.AdminService;
import farmconnect.farmconnectbackend.service.DealService;
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
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final DealService dealService;
    private final AdminService adminService;

    // 승인대기 목록 보기
    @GetMapping("/waiting/list")
    public ResponseEntity waitingList() {
        List<UserWaiting> waitingList = userService.getUserWaitingList();

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, "회원가입 대기 목록", waitingList), HttpStatus.OK);
    }

    // 승인대기 목록 보기
    @PostMapping("/waiting/permit")
    public ResponseEntity permit(@RequestBody HashMap<String, String> param) {
        Long waitingId = Long.parseLong(param.get("waitingId"));
        Boolean isPermit = Boolean.parseBoolean(param.get("isPermit"));
        Long result = userService.permitUser(waitingId, isPermit);

        return result != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "승인이 완료되었습니다"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @GetMapping("/deal/read/list")
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

    @GetMapping("/deal/read/{dealId}")
    public ResponseEntity getCurrentDeal(@RequestAttribute("userUid") String userUid, @PathVariable("dealId") Long dealId) throws IOException {
        CurrDealResponseDto currDealResponseDto = dealService.getCurrentDeal(dealId, userUid);

        return currDealResponseDto != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 거래 조회 완료", currDealResponseDto), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 거래 없음", currDealResponseDto), HttpStatus.OK);
    }

    @PostMapping("/deal/update")
    public ResponseEntity updateDeal(@RequestBody UpdateDealDto updateDealDto) throws IOException {
        Long id = adminService.updateDeal(updateDealDto);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "거래 수정 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @PostMapping("/deal/delete")
    public ResponseEntity deleteDeal(@RequestBody HashMap<String, Long> param) throws IOException {
        Long dealId  = param.get("dealId");
        adminService.deleteDeal(dealId);

        return true ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 거래 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 거래 없음"), HttpStatus.OK);
    }

    @GetMapping("/crop/read/list")
    public ResponseEntity getCropItemByKeyword(@RequestParam("keyword") Optional<String> keywordParam) throws IOException {
        String keyword = keywordParam.orElse("");
        List<String> crops = dealService.getCropItemList(keyword);

        return crops.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "관련 작물 조회 완료", crops), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "조회된 작물 없음", new ArrayList()), HttpStatus.OK);
    }

    @PostMapping("/crop/delete")
    public ResponseEntity deleteCrop(@RequestBody HashMap<String, String> param) throws IOException {
        String crop = param.get("crop");
        adminService.deleteCrop(crop);

        return true ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "해당 작물 및 관련 거래 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 작물 없음"), HttpStatus.OK);
    }
}
