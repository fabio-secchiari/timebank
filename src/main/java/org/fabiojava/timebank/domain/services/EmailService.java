package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.dto.EmailDto;

public interface EmailService {
    void sendSimpleMessage(EmailDto emailDto);
}