package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordDto {
    public String username;
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",message = "Mật khẩu phải có ít nhất 8 ký tự và bao gồm ít nhất 1 ký tự đặc biệt (!@#$%^&*)")
    public String password;

    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",message = "Mật khẩu phải có ít nhất 8 ký tự và bao gồm ít nhất 1 ký tự đặc biệt (!@#$%^&*)")
    public String duplicatePassword;
}
