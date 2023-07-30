package com.spring.blog.repository;

import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyRepository {

    List<ReplyResponseDTO> findAllByBlogId(long blogId);

    // 댓글번호 입력시 특정 댓글 하나만 가져오는 메서드 findByReplyId() 선언
    ReplyResponseDTO findByReplyId(long replyId);

    // 삭제는 replyId를 이용해 삭제한다. DeleteByReplyId() 선언
    void deleteByReplyId(long replyId);

    // insert구문은 ReplyInsertDTO를 이용해준다. save() 선언
    // ReplyInsertDTO에 내장된 멤버변수인 blogId(몇번글에), replyWriter,replyContent를
    // 쿼리문에 전달해서 INSERT구문을 완성시키기
    void save(ReplyCreateRequestDTO replyCreateRequestDTO);

    // 수정로직은 ReplyUpdateDTO를 이용해 update() 선언
    // 수정로직은 replyId를 WHERE절에 집어넣고, replyWriter,replyContent의 내용을 업데이트 해주고
    // updateAt 역시 NOW()로 바꿔준다
    void update(ReplyUpdateRequestDTO replyUpdateRequestDTO);

    // blogId를 받아서 특정 글과 연결된 댓글 전체를 삭제하는 메서드 정의
    void deleteByBlogId(long blogId);
}
