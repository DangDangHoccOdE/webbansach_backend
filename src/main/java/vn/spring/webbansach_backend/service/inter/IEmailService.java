package vn.spring.webbansach_backend.service.inter;

public interface IEmailService {
    public void sendMessage(String from, String to, String subject, String text);
}
