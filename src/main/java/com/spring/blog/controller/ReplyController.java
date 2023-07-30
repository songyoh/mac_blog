package com.spring.blog.controller;

import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.exception.NotFoundReplyByReplyIdException;
import com.spring.blog.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    // 컨트롤러는 서비스를 호출
    ReplyService replyService;

    public ReplyController(ReplyService replyService){
        this.replyService = replyService;
    }

    // 글 번호에 맞는 전체 댓글을 가져오는 메서드
    // 어떤 자원에 접근할 것인지만 uri에 명시(메서드가 행동을 결정함)
    // http://localhost:8080/reply/{blogId}/all
    @RequestMapping(value = "/{blogId}/all", method = RequestMethod.GET)
    // rest서버는 응답시 응답코드와 응답객체를 넘기기 때문에 ResponseEntity<자료형>을 리턴
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplies(@PathVariable long blogId){
        // 서비스에서 리플 목록을 들고온다
        List<ReplyResponseDTO> replies = replyService.findAllByBlogId(blogId);
        return ResponseEntity.ok()//replies)
                             .body(replies);
    }

    // replyId를 주소에 포함시켜서 요청하면 해당 번호 댓글 정보를 JSON으로 리턴하는 메서드
    @RequestMapping(value = "/{replyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findByReplyId(@PathVariable long replyId) {

        // 서비스에서 특정 번호 리플을 가져온다
        ReplyResponseDTO replyResponseDTO = replyService.findByReplyId(replyId);
        if (replyResponseDTO == null) {
            try {
                throw new NotFoundReplyByReplyIdException("없는 리플 번호를 조회했습니다.");
            } catch (NotFoundReplyByReplyIdException e) {
                e.printStackTrace(); // 예외 발생했을 때 콘솔에 뜨는 메세지를 보고싶을 때 사용하는 구문
                return new ResponseEntity<>("찾는 댓글이 없습니다.", HttpStatus.NOT_FOUND);
            }
        }
        //return new ResponseEntity<ReplyResponseDTO>(replyResponseDTO, HttpStatus.OK);
        return ResponseEntity.ok(replyResponseDTO);
    }

    // post방식으로 /reply 주소로 요청이 들어왔을때 실행되는 insertReply()메서드
    @RequestMapping(value = "", method = RequestMethod.POST) // Rest컨트롤러는 데이터를 JSON으로 주고받음
    public ResponseEntity<String> insertReply(@RequestBody ReplyCreateRequestDTO replyCreateRequestDTO){
        //System.out.println("데이터가 들어오는지 확인: "+ replyCreateRequestDTO);
        replyService.save(replyCreateRequestDTO);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    // delete 방식으로 /reply/{댓글번호} 주소로 요청이 들어왔을 때 실행되는 deleteReply() 메서드
    @RequestMapping(value = {"/{replyId}", "/{replyId}/"}, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteReply(@PathVariable long replyId){
        replyService.deleteByReplyId(replyId);

        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }


}
