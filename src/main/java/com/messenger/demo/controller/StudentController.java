package com.messenger.demo.controller;

import com.messenger.demo.dto.ChatDto;
import com.messenger.demo.dto.MessageDto;
import com.messenger.demo.dto.PostDto;
import com.messenger.demo.dto.StudentDto;
import com.messenger.demo.service.ChatService;
import com.messenger.demo.service.MessageService;
import com.messenger.demo.service.PostService;
import com.messenger.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final PostService postService;
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping("/my-profile")
    public StudentDto getCurrentProfile(Principal principal) {
        return studentService.findStudentByLogin(principal.getName());
    }

    @GetMapping("/{id}")
    public StudentDto getStudentProfile(@PathVariable("id") Long id) {
        return studentService.findStudentById(id);
    }

    @GetMapping("/all")
    public List<StudentDto> getStudentsList(@RequestParam("size") Integer pageSize, @RequestParam("page") Integer pageNumber,
                                            @RequestParam("sort") String sort) {
        return studentService.findAllStudents(pageSize, pageNumber, sort);
    }

    @GetMapping("/new")
    public StudentDto createNewStudent() {
        return studentService.getNewStudent();
    }

    @PostMapping
    public void submitStudentForm(@Valid @RequestBody StudentDto studentDto) {
        studentService.saveStudent(studentDto);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);
    }

    @GetMapping("/{id}/friends")
    public List<StudentDto> getFriendsList(@PathVariable("id") Long id) {
        return studentService.getFriendsByStudentId(id);
    }

    @GetMapping("/my-profile/friends")
    public List<StudentDto> getFriendsList(Principal principal) {
        return studentService.getFriendsByStudentLogin(principal.getName());
    }

    @PostMapping("/my-profile/friends")
    public void addFriend(Principal principal, @RequestParam("friend_id") Long friendId) {
        studentService.addNewFriend(principal.getName(), friendId);
    }

    @DeleteMapping("/my-profile/friends/{friend_id}")
    public void deleteFriend(Principal principal, @PathVariable("friend_id") Long friendId) {
        studentService.deleteFriend(principal.getName(), friendId);
    }

    @GetMapping("/my-profile/posts")
    public List<PostDto> getCurrentStudentPosts(Principal principal) {
        return studentService.getPostsByStudentLogin(principal.getName());
    }

    @GetMapping("/{id}/posts")
    public List<PostDto> getStudentPosts(@PathVariable("id") Long id) {
        return studentService.getPostsByStudentId(id);
    }

    @PostMapping("/my-profile/posts")
    public void addNewPost(Principal principal, @Valid @RequestBody PostDto postDto) {
        postService.createNewPost(principal.getName(), postDto);
    }

    @DeleteMapping("/my-profile/posts/{post_id}")
    public void deletePost(Principal principal, @PathVariable("post_id") Long postId) {
        postService.deletePost(principal.getName(), postId);
    }

    @GetMapping("/my-profile/chats")
    public List<ChatDto> getStudentChats(Principal principal) {
        return studentService.getChatsByStudentLogin(principal.getName());
    }

    @PostMapping("/my-profile/chats")
    public void addNewChat(Principal principal, @Valid @RequestBody ChatDto chatDto) {
        chatService.createNewChat(principal.getName(), chatDto);
    }

    @DeleteMapping("/my-profile/chats/{chat_id}")
    public void deleteChat(@PathVariable("chat_id") Long chatId) {
        chatService.deleteChat(chatId);
    }

    @PostMapping("/my-profile/{chat_id}/messages")
    public void addNewMessage(Principal principal, @PathVariable("chat_id") Long chatId,
                              @Valid @RequestBody MessageDto messageDto) {
        messageService.createNewMessage(principal.getName(), chatId, messageDto);
    }

    @DeleteMapping("/my-profile/{chat_id}/messages/{message_id}")
    public void deleteMessage(Principal principal, @PathVariable("chat_id") Long chatId,
                              @PathVariable("message_id") Long messageId) {
        messageService.deleteMessage(principal.getName(), chatId, messageId);
    }
}
