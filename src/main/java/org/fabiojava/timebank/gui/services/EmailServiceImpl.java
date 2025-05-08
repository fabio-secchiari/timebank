package org.fabiojava.timebank.gui.services;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.EmailDto;
import org.fabiojava.timebank.domain.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;

@Log
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendSimpleMessage(EmailDto emailDto) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(emailDto.getToList());
            mimeMessageHelper.setSubject(emailDto.getSubject());
            mimeMessageHelper.setText(emailDto.getBody());

            emailSender.send(mimeMessage);
            log.log(Level.INFO, "sendEmail() | Email sent successfully");
        } catch (Exception e) {
            log.log(Level.SEVERE, "sendEmail() | Error : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}