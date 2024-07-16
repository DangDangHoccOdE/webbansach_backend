package vn.spring.webbansach_backend.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long cartItemId;
    private int quantity;
    private Long userId;
    private Integer bookId;
}
