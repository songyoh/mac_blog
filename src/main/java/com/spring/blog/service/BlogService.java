package com.spring.blog.service;

import com.spring.blog.entity.Blog;

import java.util.List;

public interface BlogService {

    // 비즈니스 로직을 담당할 메서드를 "정의"만 하면된다.
    // 전체 블로그 포스팅을 조회하는 메서드 findAll()을 선언
    List<Blog> findAll();

    // 단일 포스팅을 조회는 메서드 FindById() 선언
    Blog findById(long blogId);

    // 단일 포스팅을 삭제하는 메서드 DeleteById() 선언
    void deleteById(long blogId);

    // 단일 포스팅을 게시하는 메서드 save() 선언
    void save(Blog blog);

    // 특정 포스팅을 수정하는 메서드 update() 선언
    void update(Blog blog);

}
