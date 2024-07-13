package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WishListDto {
    private Long userId;

    private int wishListId;

    @NotBlank(message = "Tên danh sách yêu thích không được bỏ trống!")
    private String newWishListName;

}
