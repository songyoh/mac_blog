package com.spring.blog.repository;

import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @Transactional
    @DisplayName("2번글에 연동된 댓글의 개수가 4개인지 확인")
    public void findAllByBlogIdTest(){
        // given : 2번 글을 조회하기 위한 FIxture 저장
        long blogId = 2;
        // when : findByBlogId() 호출 및 결과 자료 저장
        List<ReplyResponseDTO> result = replyRepository.findAllByBlogId(blogId);
        // then : 2번글에 연동된 댓글이 4개일 것이라 단언
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번 자료의 댓글은 3번이고, 글쓴이는 '릴리")
    public void findByReplyIdTest(){
        // given
        long replyId = 3;
        // when
        ReplyResponseDTO result = replyRepository.findByReplyId(replyId);
        // then
        assertEquals("릴리", result.getReplyWriter());
        assertEquals(3,result.getReplyId());
    }

    @Test
    @Transactional
    @DisplayName("2번 글에 연동된 댓글번호 2번을 삭제한 뒤, 2번글에 연동된 데이터 개수가 3개이고, 2번 댓글 조회시 NULL")
    public void deleteByReplyIdTest(){
        // given : 글번호 2번, 댓글번호 2번 생성
        long blogId = 2;
        long replyId = 2;

        // when : 댓글 삭제
        replyRepository.deleteByReplyId(replyId);

        // then : 2번글에 연동된 댓글 개수는 3개, 2번 댓글 재 조회시 null
        assertEquals(3, replyRepository.findAllByBlogId(blogId).size());
        assertNull(replyRepository.findByReplyId(replyId));
    }

    @Test
    @Transactional
    @DisplayName("픽스쳐를 이용해 INSERT후, 전체데이터를 가져와 마지막인덱스 번호 요소를 얻어와 입력했던 픽스쳐와 비교하면 같다")
    public void saveTest(){
        // given : 픽스처 세팅한 다음 ReplyCreateRequestDTO 생성 후 멤버변수 초기화
        long blogId = 1;
        String replyWriter = "토비의스프링";
        String replyContent = "도비이즈프리";
        ReplyCreateRequestDTO replyCreateRequestDTO = ReplyCreateRequestDTO.builder()
                .blogId(blogId)
                .replyWriter(replyWriter)
                .replyContent(replyContent)
                .build();

        // when : insert실행
        replyRepository.save(replyCreateRequestDTO);

        // then : blogId번 글의 전체 댓글을 가지고 온 다음 마지막 인덱스 요소만 변수에 저장한 뒤
        // getter를 이용해 위에서 넣은 픽스쳐와 일치하는지 체크
        List<ReplyResponseDTO> resultList = replyRepository.findAllByBlogId(blogId);
        // resultList의 개수 - 1 이 마지막 인덱스 번호이므로, resultList에서 마지막 인덱스 요소만 가져오기
        ReplyResponseDTO result = resultList.get(resultList.size() - 1);
        // 단언문 작성
        assertEquals(replyWriter, result.getReplyWriter());
        assertEquals(replyContent, result.getReplyContent());
    }

    @Test
    @Transactional
    @DisplayName("fixture로 수정할 댓글쓴이, 댓글내용, 3번 replyId를 지정합니다. 수정 후 3번자료를 DB에서 꺼내 fixture비교 및 published_at과 updated_at이 다른지 확인")
    // "fixture로 수정할 댓글쓴이, 댓글내용, 3번 replyId를 지정합니다.
    // 수정 후 3번자료를 DB에서 꺼내 fixture비교 및 published_at과 updated_at이 다른지 확인"
    // DTO를 만들어서 거기다가 수정자료 자료 집어넣기
    public void updateTest(){
        // given
        long replyId = 3;
        String replyWriter = "수정글쓴잉";
        String replyContent = "수정한내용물!";
        ReplyUpdateRequestDTO replyUpdateRequestDTO = ReplyUpdateRequestDTO.builder()
                .replyId(replyId)
                .replyWriter(replyWriter)
                .replyContent(replyContent)
                .build();

        // when
        replyRepository.update(replyUpdateRequestDTO);

        //then
        ReplyResponseDTO result = replyRepository.findByReplyId(replyId);
        assertEquals(replyWriter, result.getReplyWriter());
        assertEquals(replyContent, result.getReplyContent());
        assertTrue(result.getUpdatedAt().isAfter(result.getPublishedAt()));
    }

    @Test
    @Transactional
    @DisplayName("blogId가 2번인 글을 삭제하면, 삭제한 글의 전체 댓글 조회시 자료 길이는 0일 것이다.")
    public void deleteByBlogId(){
        // given
        long blogId = 2;

        // when
        replyRepository.deleteByBlogId(blogId);

        // then : blogId 2번 글 전체 댓글을 얻어와서 길이가 0인지 확인
        List<ReplyResponseDTO> resultList = replyRepository.findAllByBlogId(blogId);
        assertEquals(0, resultList.size());
    }

    // 블로그와 함께 댓글이 삭제되는지 테스트코드를 한번 더 작성해보길 추천 - 강사님 말씀

}