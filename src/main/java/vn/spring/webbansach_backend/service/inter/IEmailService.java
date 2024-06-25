package vn.spring.webbansach_backend.service.inter;

public interface IEmailService {
     void sendMessage(String from, String to, String subject, String text);

}
