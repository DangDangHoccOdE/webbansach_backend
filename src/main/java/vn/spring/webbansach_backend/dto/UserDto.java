package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.spring.webbansach_backend.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
        private int userId;

        @NotBlank(message = "Tên không được bỏ trống! ")
        private String firstName;

        @NotBlank(message = "Họ đệm không được bỏ trống! ")
        private String lastName;

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "Ngày sinh không đúng định dạng: yyyy/MM/dd!")
        private String dateOfBirth;

        @NotBlank(message = "Tài khoản không thể bỏ trống!")
        private String userName;

        @Pattern(regexp = "^0\\d{9}$",message = "Số điện thoại không đúng định dạng, phải có 10 chữ số và bắt đầu là 0")
        private String phoneNumber;

        @Pattern(regexp="^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",message = "Mật khẩu phải có ít nhất 8 ký tự và bao gồm ít nhất 1 ký tự đặc biệt (!@#$%^&*)")
        private String password;

        @NotBlank(message = "Giới tính không được bỏ trống!")
        private String sex;

        @Pattern(regexp = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$",message = "Định dạng email không hợp lệ!")
        private String email;

        private String deliveryAddress;

        private String purchaseAddress;

        private List<Review> reviewList;

        private List<WishList> wishListList;

        private List<Order> orderList;

        private List<Role> roleList;

        private boolean isActive;

        private String activationCode;

        private LocalDateTime activationExpiry;

        private String avatar;

        @NotNull
        private String authProvider;
}
