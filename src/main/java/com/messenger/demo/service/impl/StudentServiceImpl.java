package com.messenger.demo.service.impl;

import com.messenger.demo.dao.StudentRepository;
import com.messenger.demo.dto.ChatDto;
import com.messenger.demo.dto.PostDto;
import com.messenger.demo.dto.StudentDto;
import com.messenger.demo.entity.Student;
import com.messenger.demo.exception.PageNotFoundException;
import com.messenger.demo.exception.StudentNotFoundException;
import com.messenger.demo.mapper.ChatMapper;
import com.messenger.demo.mapper.PostMapper;
import com.messenger.demo.mapper.StudentMapper;
import com.messenger.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PostMapper postMapper;
    private final ChatMapper chatMapper;
    private StudentService studentService;

    @Autowired
    public void setStudentService(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAllStudents(Integer pageSize, Integer pageNumber, String sort) {
        String[] sortParams = sort.split(",");
        Pageable sortedByPriceDesc = null;
        if (sortParams[1].equals("desc")) {
            sortedByPriceDesc = PageRequest.of(pageNumber, pageSize, Sort.by(sortParams[0]).descending());
        }
        if (sortParams[1].equals("asc")) {
            sortedByPriceDesc = PageRequest.of(pageNumber, pageSize, Sort.by(sortParams[0]).ascending());
        }
        Page<Student> students = studentRepository.findAll(sortedByPriceDesc);
        if (pageNumber > students.getTotalPages()) {
            throw new PageNotFoundException();
        }
        return StreamSupport.stream(students.spliterator(), true)
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto getNewStudent() {
        return new StudentDto();
    }

    @Override
    @Transactional
    public void saveStudent(StudentDto studentDto) {
        studentRepository.save(studentMapper.toEntity(studentDto));
    }

    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        Student student = studentService.findStudentEntityById(id);
        student.getSchool().getStudentList().remove(student);
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findStudentById(Long id) {
        return studentMapper.toDto(studentService.findStudentEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findStudentByLogin(String login) {
        return studentMapper.toDto(studentService.findStudentEntityByLogin(login));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getFriendsByStudentId(Long id) {
        Student student = studentService.findStudentEntityById(id);
        return student.getFriendList().stream().map(studentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getFriendsByStudentLogin(String login) {
        Student student = studentService.findStudentEntityByLogin(login);
        return student.getFriendList().stream().map(studentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addNewFriend(String login, Long friendId) {
        Student currentStudent = studentService.findStudentEntityByLogin(login);
        Student newFriend = studentService.findStudentEntityById(friendId);
        if (currentStudent.getFriendList() == null) {
            currentStudent.setFriendList(new ArrayList<>());
        }
        if (newFriend.getFriendList() == null) {
            newFriend.setFriendList(new ArrayList<>());
        }
        currentStudent.getFriendList().add(newFriend);
        newFriend.getFriendList().add(currentStudent);
        studentRepository.save(currentStudent);
    }

    @Override
    @Transactional
    public void deleteFriend(String login, Long friendId) {
        Student currentStudent = studentService.findStudentEntityByLogin(login);
        Student oldFriend = studentService.findStudentEntityById(friendId);
        if (currentStudent.getFriendList().contains(oldFriend)
                && oldFriend.getFriendList().contains(currentStudent)) {
            currentStudent.getFriendList().remove(oldFriend);
            oldFriend.getFriendList().remove(currentStudent);
            studentRepository.save(currentStudent);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByStudentId(Long id) {
        Student student = studentService.findStudentEntityById(id);
        return student.getPostList().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByStudentLogin(String login) {
        Student student = studentService.findStudentEntityByLogin(login);
        return student.getPostList().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatDto> getChatsByStudentLogin(String login) {
        Student student = studentService.findStudentEntityByLogin(login);
        return student.getChatList().stream().map(chatMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Student findStudentEntityById(Long id) {
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Student findStudentEntityByLogin(String login) {
        return Optional.of(studentRepository.findByLogin(login)).orElseThrow(StudentNotFoundException::new);
    }

}
