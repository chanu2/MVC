package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.CropItemDto;
import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.dto.response.MainStatResponseDto;
import farmconnect.farmconnectbackend.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stat")
public class StatController {
    private final StatService statService;

    @GetMapping("/main")
    public ResponseEntity getMainStat() throws IOException {
        MainStatResponseDto mainStatResponse = statService.getMainStat();

        return mainStatResponse != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "메인 통계 반환 완료", mainStatResponse), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "통계 없음"), HttpStatus.OK);
    }

    @GetMapping("/crop/avg")
    public ResponseEntity getCropAvg() throws IOException {
        List<CropItemDto> crops = statService.getCropItemAvg();

        return crops != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "작물 평균 통계 반환 완료", crops), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "통ty890계 없음"), HttpStatus.OK);
    }

}
