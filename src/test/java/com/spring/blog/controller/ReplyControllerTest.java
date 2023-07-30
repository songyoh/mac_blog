package com.spring.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MVC 테스트는 브라우저를 켜야 원래 테스트가 가능하므로 브라우저를 대체할 객체를 만들어 수행
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired // 데이터 직렬화에 사용하는 객체 - insert로직 구현시 추가된 자료
    private ObjectMapper objectMapper;

    // 일시적으로 Repository를 생성 - delete로직 구현시 추가된 자료
    // 레포지토리 레이어의 메서드는 쿼리문을 하나만 호출하는것이 보장되지만
    // 서비스 레이어의 메서드는 추후에 쿼리문을 두 개 이상 호출할 수도 있고, 그런 변경이 생겼을때 testcode도 같이 수정할 가능성이 생김
    @Autowired
    private ReplyRepository replyRepository;

    // 컨트롤러를 테스트 해야하는데 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않는다.
    // 각 테스트전에 설정하기
    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    @DisplayName("2번 글에 대한 전체 댓글을 조회시 0번째 요소의 replyWriter, replyId 맞는지 여부 조회")
    void findAllReplies() throws Exception{ // mockMvc의 예외를 던져줄 Exception
        // given : 픽스쳐 설정, 접속 주소 설정
        String replyWriter = "댓글쓴이";
        long replyId = 1;
        String url = "/reply/2/all";

        // when: 해당 주소로 접속 후 json 데이터 리턴받아 저장하기. ResultActions형 자료로 json 저장
        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then : 리턴받은 json 목록의 0번째 요소의 replyWriter, replyId 예상과 일치하는지 확인
        result.andExpect(status().isOk()) // 상태코드 : 200
                .andExpect(jsonPath("$[0].replyWriter").value(replyWriter)) // $[0] : json 전체데이터를 불러오는 것, 0번째 지목
                .andExpect(jsonPath("$[0].replyId").value(replyId));
    }

    @Test
    @Transactional
    @DisplayName("replyId 2번 조회시 얻어온 json객체의 ReplyWriter 미미, replyId 2번")
    public void findByReplyIdTest() throws Exception{
        // given
        String replyWriter = "미미";
        long replyId = 2;
        String url = "/reply/2";

        // when : 위에 설정한 Url로 접속후 json 데이터 리턴받아 저장, ResultAction형 자료로 json 저장
        // fetch(url, {method:'get'}).then(res => res.json());에 대응하는 코드
        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.replyWriter").value(replyWriter)) // 단일 Json객체 $생략 가능
                .andExpect(jsonPath("$.replyId").value(replyId));
    }

    @Test
    @Transactional
    @DisplayName("blogId 1번의 replyWriter는 '주주' replyContent는 '안뇽'을 등록 후 전체댓글 조회시 일치")
    public void insertReplyTest() throws Exception{
        //given : 픽스쳐 생성 및 ReplyCreateRequestDTO 객체 생성 후 픽스처 주입 + json으로 데이터 직렬화
        long blogId = 1;
        String replyWriter = "주주";
        String replyContent = "안뇽";
        ReplyCreateRequestDTO replyCreateRequestDTO = new ReplyCreateRequestDTO(blogId, replyWriter, replyContent);
        String url = "/reply";
        String url2 = "/reply/1/all";

        // 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(replyCreateRequestDTO);

        // when : 직렬화된 데이터를 이용해 POST방식으로
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)); // 위에서 직렬화한 requestBody 변수를 전달

        //then : 위에서 blogId로 지정한 1번글에 전체 데이터를 가져와
        // 픽스쳐와 replyWriter, replyContent가 일치하는지 확인
        final ResultActions result = mockMvc.perform(get(url2)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].replyWriter").value(replyWriter))
                .andExpect(jsonPath("$[0].replyContent").value(replyContent));
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번을 삭제할 경우, 글번호 2번의 댓글수는 3개, 그리고 단일댓글 조회시 NULL")
    public void deleteReplyTest() throws Exception {
        // given
        long replyId = 3;
        long blogId = 2;
        String url = "http://localhost:8080/reply/3";

        // when : 삭제 실행
        mockMvc.perform(delete(url));//.accept(MediaType.TEXT_PLAIN)); 은 requestBody에 실어서 보내는 데이터가 있는 경우 사용
                                    // 리턴데이터가 있는경우에 해당 데이터를 어떤 형식으로 받아올지 기술

        // then : repository를 이용해 전테 데이터를 가져온 후, 개수 비교 및 삭제한 3번 댓글은 NULL이 리턴되는지 확인
        List<ReplyResponseDTO> resultList = replyRepository.findAllByBlogId(blogId);
        assertEquals(3, resultList.size());
        ReplyResponseDTO result = replyRepository.findByReplyId(replyId);
        assertNull(result);
    }

}