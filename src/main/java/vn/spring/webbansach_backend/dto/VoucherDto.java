package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VoucherDto {
    private Long voucherId;

    @NotBlank(message = "Mã voucher không được bỏ trống!")
    private String code;

    @NotBlank(message = "Mô tả voucher không được bỏ trống!")
    private String describe;

    @NotNull(message = "Phần trăm giảm giá không được bỏ trống!")
    @Max(value = 100,message = "Phần trăm giảm giá phải nhỏ hơn 100")
    @Min(value = 0,message = "Phần trăm giảm giá phải lớn hơn 0")
    private Double discountValue;

    @NotNull(message = "Số lượng không được bỏ trống!")
    @Min(value = 0,message = "Số lượng phải lớn hơn 0")
    private int quantity;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "Ngày hết không đúng định dang: yyyy/MM/dd!")
    private String expiredDate;

    @NotBlank(message = "Ảnh voucher không được bỏ trống")
    private String voucherImage;

    private Boolean isActive;
    private Boolean isAvailable;

    @NotBlank(message = "Không thể bỏ trống loại voucher")
    private String typeVoucher;

}
