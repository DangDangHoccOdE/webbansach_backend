package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDto {
    private int paymentId;

    @NotBlank(message = "Tên phương thức thanh toán không được bỏ trống")
    private String paymentName;
}
