package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class BlogServiceTest {

    @Autowired
    BlogService blogService;

    @Test
    @Transactional // 이 테스트의 결과가 DB 커밋을 하지 않음
    public void findAllTest(){
        //given
        //when : 전체 데이터 가져오기
        List<Blog> blogList = blogService.findAll();
        //then : 길이가 3일 것이다.
//        assertEquals(3, blogList.size()); 아래 코드와 동일
        assertThat(blogList.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    public void findByIdTest(){
        //given : 조회할 번호인 2번 변수에 저장, 예상되는 글쓴이, 본문정보 저장
        long blogId = 2;
        String writer = "2번유저";
        String blogTitle = "2번제목";

        //when : DB에서 2번 유저 얻어오기
        Blog blog = blogService.findById(blogId);

        //then : 얻어온 유저의 번호는 blogId변수, 글쓴이는 writer변수, 제목은 BLOGdTitle에 든 값일 것이다.
        assertEquals(blogId, blog.getBlogId());
        assertEquals(writer, blog.getWriter());
        assertEquals(blogTitle, blog.getBlogTitle());
    }

    @Test
    @Transactional
    //@Commit // 트랜잭션 적용된 테스트의 결과를 커밋해서 디비에 반영하도록 만듦
    public void deleteByIdTest(){
        //given : 삭제할 번호 2번 지정
        long blogId = 2;

        //when
        blogService.deleteById(blogId);

        //then : 삭제 되었다면, 총 게시글은 2개, 2번으로 재 조회시 NULL
        assertEquals(2, blogService.findAll().size());
        assertNull(blogService.findById(blogId));
    }

    @Test
    @Transactional
    public void saveTest(){
        //given : Blog객체에 필요데이터인 writer, blogTitle, blogContent를 주입해 builder 패턴 생성
        String writer = "서비스글쓴이";
        String blogTitle = "추가된제목";
        String blogContent = "추가된본문";
        int lastBlogIndex = 3;
        Blog blog = Blog.builder()
                .writer(writer)
                .blogTitle(blogTitle)
                .blogContent(blogContent)
                .build();

        //when : .save()를 호출해 DB에 저장
        blogService.save(blog);

        //then : 전체 요소의 개수 4개, 현재 얻어온 마지막 포스팅 생성시 사용한 자료와 일치하는지 확인
        assertEquals(4, blogService.findAll().size());
        assertEquals(writer, blogService.findAll().get(lastBlogIndex).getWriter());
        assertEquals(blogTitle, blogService.findAll().get(lastBlogIndex).getBlogTitle());
        assertEquals(blogContent, blogService.findAll().get(lastBlogIndex).getBlogContent());
    }

    @Test
    @Transactional
    public void updateTest(){
        //given : blogId 2번글의 제목, 본문을 수정하기 위한 픽스쳐 선언 및 Blog 객체 선언
        long blogId = 2;
        String blogTitle = "수정된제목";
        String blogContent = "수정된본문";

        Blog blog = Blog.builder()
                .blogId(blogId)
                .blogTitle(blogTitle)
                .blogContent(blogContent)
                .build();

        //when : update()메서드를 이용해 상단 BLOG 객체를 파라미터로 수정 실행
        blogService.update(blog);

        //then : blogId번 글을 가져와서, blogTitle, blogContent가 수정을 위한 픽스쳐와 동일하다고 단언
        assertEquals(blogId, blogService.findById(blogId).getBlogId());
        assertEquals(blogTitle, blogService.findById(blogId).getBlogTitle());
        assertEquals(blogContent, blogService.findById(blogId).getBlogContent());
    }
}
