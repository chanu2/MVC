package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity uploadImage(@RequestParam("files") List<MultipartFile> multipartFile) {
        List<String> nameList = imageService.uploadFile(multipartFile);

        return new ResponseEntity(DefaultRes.res(StatusCode.OK, "이미지 업로드 완료", nameList), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity removeImage(@RequestBody HashMap<String, String> param) {
        String fileName = param.get("fileName");
        imageService.deleteFile(fileName);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, "이미지 삭제 완료"), HttpStatus.OK);
    }
}
