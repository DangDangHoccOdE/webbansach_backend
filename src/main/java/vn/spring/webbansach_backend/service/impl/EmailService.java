package vn.spring.webbansach_backend.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.service.inter.IEmailService;

@Service
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMessage(String from, String to, String subject, String text) {
        // MimeMailMessage => can add media
        // SimpleMailMessage=> text
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        javaMailSender.send(mailMessage);
    }
}
