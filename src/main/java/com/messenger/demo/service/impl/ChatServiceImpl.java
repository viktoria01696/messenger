package com.messenger.demo.service.impl;

import com.messenger.demo.dao.ChatRepository;
import com.messenger.demo.dto.ChatDto;
import com.messenger.demo.dto.MessageDto;
import com.messenger.demo.dto.StudentDto;
import com.messenger.demo.entity.Chat;
import com.messenger.demo.entity.Student;
import com.messenger.demo.exception.ChatNotFoundException;
import com.messenger.demo.mapper.ChatMapper;
import com.messenger.demo.mapper.MessageMapper;
import com.messenger.demo.mapper.StudentMapper;
import com.messenger.demo.service.ChatService;
import com.messenger.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final StudentService studentService;
    private final ChatMapper chatMapper;
    private final StudentMapper studentMapper;
    private final MessageMapper messageMapper;
    private ChatServiceImpl chatService;

    @Autowired
    public void setChatService(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @Override
    @Transactional
    public void createNewChat(String login, ChatDto chatDto) {
        Chat chat = chatMapper.toEntity(chatDto);
        Student student = studentService.findStudentEntityByLogin(login);
        chat.setStudentList(new ArrayList<>());
        chat.getStudentList().add(student);
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        Chat chat = chatService.findChatEntityById(chatId);
        chatRepository.delete(chat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getChatMembers(Long id) {
        Chat chat = chatService.findChatEntityById(id);
        return chat.getStudentList().stream().map(studentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addChatMember(Long chatId, Long studentId) {
        Chat chat = chatService.findChatEntityById(chatId);
        Student student = studentService.findStudentEntityById(studentId);
        if (chat.getStudentList() == null) {
            chat.setStudentList(new ArrayList<>());
        }
        if (student.getChatList() == null) {
            student.setChatList(new ArrayList<>());
        }
        chat.getStudentList().add(student);
        student.getChatList().add(chat);
        chatRepository.save(chat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(Long id) {
        Chat chat = chatService.findChatEntityById(id);
        return chat.getMessageList().stream().map(messageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Chat findChatEntityById(Long id) {
        return chatRepository.findById(id).orElseThrow(ChatNotFoundException::new);
    }

}
