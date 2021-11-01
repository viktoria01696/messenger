package com.messenger.demo.service.impl;

import com.messenger.demo.dao.MessageRepository;
import com.messenger.demo.dto.MessageDto;
import com.messenger.demo.entity.Chat;
import com.messenger.demo.entity.Message;
import com.messenger.demo.entity.Student;
import com.messenger.demo.exception.PostNotFoundException;
import com.messenger.demo.mapper.MessageMapper;
import com.messenger.demo.service.ChatService;
import com.messenger.demo.service.MessageService;
import com.messenger.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final StudentService studentService;
    private final ChatService chatService;
    private final MessageMapper messageMapper;
    private MessageServiceImpl messageService;

    @Autowired
    public void setMessageService(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public void createNewMessage(String login, Long chatId, MessageDto messageDto) {
        Student student = studentService.findStudentEntityByLogin(login);
        Chat chat = chatService.findChatEntityById(chatId);
        Message message = messageMapper.toEntity(messageDto);
        if (chat.getMessageList() == null) {
            chat.setMessageList(new ArrayList<>());
        }
        if (student.getMessageList() == null) {
            student.setMessageList(new ArrayList<>());
        }
        message.setChat(chat);
        message.setStudent(student);
        chat.getMessageList().add(message);
        student.getMessageList().add(message);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(String login, Long chatId, Long messageId) {
        Student student = studentService.findStudentEntityByLogin(login);
        Chat chat = chatService.findChatEntityById(chatId);
        Message message = messageService.findMessageEntityById(messageId);
        student.getMessageList().remove(message);
        chat.getMessageList().remove(message);
        messageRepository.delete(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Message findMessageEntityById(Long id) {
        return messageRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

}
