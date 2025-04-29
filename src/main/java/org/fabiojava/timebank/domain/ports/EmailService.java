package org.fabiojava.timebank.domain.ports;

import org.fabiojava.timebank.domain.dto.EmailDto;

public interface EmailService {
    void sendSimpleMessage(EmailDto emailDto);
}