package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import com.spring.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService{

    BlogRepository blogRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository){
        this.blogRepository = blogRepository;
    }

    @Override
    public List<Blog> findAll() {
//        List<Blog> blogList = blogRepository.findAll();
//        return blogList;
        return blogRepository.findAll();
    }

    @Override
    public Blog findById(long blogId) {
        return blogRepository.findById(blogId);
    }

    @Override
    public void deleteById(long blogId) {
        blogRepository.deleteById(blogId);
    }

    @Override
    public void save(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public void update(Blog blog) {
        blogRepository.update(blog);
    }
}
