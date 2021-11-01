package com.messenger.demo.service;

import com.messenger.demo.dto.ChatDto;
import com.messenger.demo.dto.PostDto;
import com.messenger.demo.dto.StudentDto;
import com.messenger.demo.entity.Student;

import java.util.List;

public interface StudentService {

    List<StudentDto> findAllStudents(Integer pageSize, Integer pageNumber, String sort);

    StudentDto getNewStudent();

    void saveStudent(StudentDto studentDto);

    void deleteStudentById(Long id);

    StudentDto findStudentById(Long id);

    StudentDto findStudentByLogin(String login);

    List<StudentDto> getFriendsByStudentId(Long id);

    List<StudentDto> getFriendsByStudentLogin(String login);

    List<PostDto> getPostsByStudentId(Long id);

    List<PostDto> getPostsByStudentLogin(String login);

    List<ChatDto> getChatsByStudentLogin(String login);

    void addNewFriend(String login, Long friendId);

    void deleteFriend(String login, Long friendId);

    Student findStudentEntityById(Long id);

    Student findStudentEntityByLogin(String login);

}
