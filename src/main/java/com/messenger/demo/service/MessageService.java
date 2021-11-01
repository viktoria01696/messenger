package com.messenger.demo.service;

import com.messenger.demo.dto.MessageDto;
import com.messenger.demo.entity.Message;

public interface MessageService {

    void createNewMessage(String login, Long chatId, MessageDto messageDto);

    void deleteMessage(String login, Long chatId, Long messageId);

    Message findMessageEntityById(Long id);

}
