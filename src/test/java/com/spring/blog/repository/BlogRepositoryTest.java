package com.spring.blog.repository;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // DROP TABLE시 필요한 어노테이션
public class BlogRepositoryTest {

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // 각 테스트 전에 공통적으로 실행할 코드를 저장해두는 곳
    public void setBlogRepository(){
        blogRepository.createBlogTable(); // blog 테이블 생성
        blogRepository.insertTestData(); // 생성된 blog 테이블에 더미데이터(xml) 3개 입력
    }

    @Test
    @DisplayName("전체 행을 얻어오고, 그 중 자바 1번 인덱스 행만 추출해 번호 확인")
    public void findAllTest(){
        //given : (사람기준)2번 요소 조회를 위한 fixture 선언
        int blogId = 1; // 자바 자료구조 인덱스는 0번부터
        //when
        List<Blog> blogList = blogRepository.findAll();
        //System.out.println(blogList);

        //then
        assertEquals(3, blogList.size());
        // (사람기준)2번째 객체의 ID번호는 2번일 것이다
        assertEquals(2, blogList.get(blogId).getBlogId());
    }

    @Test
    @DisplayName("2번 글 조회시, 제목,글쓴이,번호가 단언대로 일치하는지 확인")
    public void findByIdTest(){
        // given : 조회할 DB기준 2번 id를 변수로 저장
        long blogId = 2;
        // when : 레포지토리에서 단일행 Blog를 얻어와 저장
        Blog blog = blogRepository.findById(blogId);
        // then : 해당 객체의 writer 멤버변수는 "2번유저", blogTitle은 "2번제목", blogId는 2
        assertEquals("2번유저", blog.getWriter());
        assertEquals("2번제목", blog.getBlogTitle());
        assertEquals(2, blog.getBlogId());
    }

    @Test
    @DisplayName("4번째 행 데이터 저장 후, 행 저장여부 및 전달데이터 저장 여부 확인 ")
    public void saveTest(){
        // given : 저장을 위한 blog entity 생성 및 writer, blogTitle,
        // blogContent 해당하는 fixture setter로 저장하기, findAll()로 얻어올 데이터의 인덱스 번호 저장
        String writer = "추가몬";
        String blogTitle = "추가제목";
        String blogContent = "추가된본문";
//        Blog blog = new Blog();
//        blog.setWriter(writer);
//        blog.setBlogTitle(blogTitle);
//        blog.setBlogContent(blogContent);

        // blog 객체 생성 코드를 빌더패턴으로 리팩토링 + builder패턴 쓰는 법
        // 장점 : 파라미터 순서를 뒤바꿔서 집어넣어도 상관없음
        Blog blog = Blog.builder() // 빌더패턴 시작
                .writer(writer)
                .blogTitle(blogTitle)
                .blogContent(blogContent)
                .build(); // 빌더패턴 끝

        int blogId = 3; // 4번째 요소 조회(자바 인덱스는 0번부터 시작)
        // when : save()메서드 호출, findAll()로 전체 데이터 가져오기\
        blogRepository.save(blog);
        List<Blog> blogList = blogRepository.findAll();
        // then : 전체 데이터 개수 4개 확인
        // 방금 입력한 INSERT한 데이터가 입력한대로 들어갔는지 단언문 확인
        assertEquals(4, blogList.size());
        assertEquals(writer, blogList.get(blogId).getWriter());
        assertEquals(blogTitle, blogList.get(blogId).getBlogTitle());
        assertEquals(blogContent, blogList.get(blogId).getBlogContent());
    }

    @Test
    @DisplayName("2번글 삭제 후 전체 목록 조회시 남은행수는 2개, 삭제한번호 재조회시 Null")
    public void deleteByIdTest(){
        // given : 삭제할 자료의 번호 저장
        long blogId = 2;
        // when : 삭제로직 실행 후, findAll(), findById()로 전체 행, 개별 행 가져오기
        blogRepository.deleteById(blogId);

        // then : 단언문을 이용해 전체 행 2개, 개별행은 null임을 확인
        assertEquals(2, blogRepository.findAll().size());
        assertNull(blogRepository.findById(blogId));
    }

    @Test
    @DisplayName("2번글의 제목을 '수정한 제목'으로, 본문도 '수정한 본문'으로 수정 후 확인")
    public void updateTest(){
        // given : 2번글 원본 데이터 얻어오기. blog 객체 생성해서 Id, Title, Content 내용만 수정
        long blogId = 2;
        String blogTitle = "수정한제목";
        String blogContent = "수정한본문";

//        Blog blog = blogRepository.findById(blogId);
        //System.out.println(blog);
//        blog.setBlogTitle(blogTitle);
//        blog.setBlogContent(blogContent);
        //System.out.println(blog);

        Blog blog = Blog.builder()
                .blogId(blogId)
                .blogTitle(blogTitle)
                .blogContent(blogContent)
                .build();

        // when : 수정내역 DB에 반영해주기
        blogRepository.update(blog);

        // then : 바뀐 2번글의 타이틀은 "수정한제목", 본문은 "수정한본문"으로 변환되었을 것이다.
//        assertEquals("수정한제목", blogRepository.findById(blogId).getBlogTitle());
//        assertEquals("수정한본문", blogRepository.findById(blogId).getBlogContent());
        assertEquals(blogTitle, blogRepository.findById(blogId).getBlogTitle());
        assertEquals(blogContent, blogRepository.findById(blogId).getBlogContent());
    }


    @AfterEach // 각 단위테스트 끝난 후에 실행할 구문을 작성
    public void dropBlogTable(){
        blogRepository.dropBlogTable(); // blog 테이블 삭제
    }
}
