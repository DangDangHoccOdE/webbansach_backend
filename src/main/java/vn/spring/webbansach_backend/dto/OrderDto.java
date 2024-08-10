package vn.spring.webbansach_backend.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.*;
@Data
public class OrderDto {
    private Long orderId;

    @NotBlank(message = "Mã đơn hàng không được bỏ trống")
    private String orderCode;;


    @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Ngày đặt hàng không đúng định dạng: yyyy/MM/dd HH:mm:ss!")
    private String date;

    @NotBlank(message = "Địa chỉ giao hàng không thể bỏ trống")
    private String deliveryAddress;

    private String deliveryStatus;

    private String orderStatus;

    private String purchaseAddress;

    @NotNull(message = "Chi phí thanh toán không được bỏ trống!")
    @Min(value = 0,message = "Chi phí thanh toán phải lớn hơn 0")
    private Double paymentCost;

    @NotNull(message = "Chi phí vận chuyển không được bỏ trống!")
    @Min(value = 0,message = "Chi phí vận chuyển phải lớn hơn 0")
    private Double shippingFee;

    @NotNull(message = "Chi phí vận chuyển áp dụng voucher không được bỏ trống!")
    @Min(value = 0,message = "Chi phí vận chuyển phải lớn hơn 0")
    private Double shippingFeeVoucher;

    @NotNull(message = "Tổng thanh toán không được bỏ trống!")
    @Min(value = 0,message = "Tổng thanh toán phải lớn hơn 0")
    private Double totalPrice;

    @NotNull(message = "Số lượng sản phẩm không được bỏ trống!")
    @Min(value = 0,message = "Số lượng sản phẩm phải lớn hơn 0")
    private Integer totalProduct;

    private String noteFromUser;

    @NotNull(message = "Người sử dụng không thể bỏ trống")
    private Long userId;

    @NotBlank(message = "Phương thức thanh toán không thể bỏ trống")
    private String paymentMethod;

    @NotBlank(message = "Phương thức vận chuyển không thể bỏ trống")
    private String deliveryMethod;

    @NotNull(message = "Mặt hàng không được bỏ trống")
    private List<Integer> cartItems;
}
