package com.messenger.demo.service;

import com.messenger.demo.dto.PostDto;
import com.messenger.demo.entity.Post;

public interface PostService {

    void createNewPost(String login, PostDto postDto);

    void deletePost(String login, Long postId);

    Post findPostEntityById(Long id);

}
