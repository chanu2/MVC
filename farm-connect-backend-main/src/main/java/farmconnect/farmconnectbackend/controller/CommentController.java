package farmconnect.farmconnectbackend.controller;

import farmconnect.farmconnectbackend.dto.response.NewCommentResponseDto;
import farmconnect.farmconnectbackend.response.DefaultRes;
import farmconnect.farmconnectbackend.response.StatusCode;
import farmconnect.farmconnectbackend.dto.AddCommentDto;
import farmconnect.farmconnectbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;


    @PostMapping("/add")
    public ResponseEntity addComment(@RequestAttribute("userUid") String userUid, @RequestBody AddCommentDto addCommentDto) throws IOException {
        Long id = null;
        id = commentService.addComment(addCommentDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "댓글 추가 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity addDeal(@RequestAttribute("userUid") String userUid, @RequestBody HashMap<String, Long> param) throws IOException {
        Long id = commentService.deleteComment(param.get("commentId"), userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "댓글 삭제 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    // fcm send test
    @PostMapping("/delete/{dealId}")
    public ResponseEntity deleteComment(@RequestAttribute("userUid") String userUid, @RequestBody AddCommentDto addCommentDto) throws IOException {
        Long id = null;
        id = commentService.addComment(addCommentDto, userUid);

        return id != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "댓글 추가 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    @GetMapping("/list/{dealId}")
    public ResponseEntity getCommentList(@PathVariable("dealId") Long dealId) throws IOException {
        List comments = commentService.getCommentByDeal(dealId);

        return comments.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "댓글 조회 완료", comments), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 댓글 없음", new ArrayList()), HttpStatus.OK);
    }

    @GetMapping("/new/list")
    public ResponseEntity getNewCommentList(@RequestAttribute("userUid") String userUid) throws IOException {
        List<NewCommentResponseDto> comments = commentService.getNewComments(userUid);

        return comments.size() != 0 ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "새댓글 조회 완료", comments), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "조회된 댓글 없음", new ArrayList()), HttpStatus.OK);
    }
}
