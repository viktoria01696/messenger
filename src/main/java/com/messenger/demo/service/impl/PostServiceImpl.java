package com.messenger.demo.service.impl;

import com.messenger.demo.dao.PostRepository;
import com.messenger.demo.dto.PostDto;
import com.messenger.demo.entity.Post;
import com.messenger.demo.entity.Student;
import com.messenger.demo.exception.PostNotFoundException;
import com.messenger.demo.mapper.PostMapper;
import com.messenger.demo.service.PostService;
import com.messenger.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final StudentService studentService;
    private final PostMapper postMapper;
    private PostServiceImpl postService;

    @Autowired
    public void setPostService(PostServiceImpl postService) {
        this.postService = postService;
    }

    @Override
    @Transactional
    public void createNewPost(String login, PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        Student student = studentService.findStudentEntityByLogin(login);
        post.setStudent(student);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(String login, Long postId) {
        Post post = postService.findPostEntityById(postId);
        Student student = studentService.findStudentEntityByLogin(login);
        student.getPostList().remove(post);
        postRepository.delete(post);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Post findPostEntityById(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }


}
