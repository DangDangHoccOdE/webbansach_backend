package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.Review;
import vn.spring.webbansach_backend.entity.Role;
import vn.spring.webbansach_backend.entity.WishList;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserOauth2Dto {
    private int userId;

    @NotBlank(message = "Tên không được bỏ trống! ")
    private String firstName;

    @NotBlank(message = "Họ đệm không được bỏ trống! ")
    private String lastName;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "Ngày sinh không đúng định dạng: yyyy/MM/dd!")
    private String dateOfBirth;

    @Pattern(regexp = "^0\\d{9}$",message = "Số điện thoại không đúng định dạng, phải có 10 chữ số và bắt đầu là 0")
    private String phoneNumber;

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
