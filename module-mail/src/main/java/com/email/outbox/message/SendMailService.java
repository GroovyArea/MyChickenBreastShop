package com.email.outbox.message;

import com.daniel.outbox.dto.MailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 이메일 전송
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailService {

    private static final String ENCODE = "utf-8";

    private final JavaMailSender javaMailSender;

    public void sendEmail(MailDTO sendMailDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODE);
            helper.setFrom(sendMailDTO.getFrom());
            helper.setTo(sendMailDTO.getTo());
            helper.setSubject(sendMailDTO.getTitle());
            helper.setText(sendMailDTO.getContent(),true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }
}
