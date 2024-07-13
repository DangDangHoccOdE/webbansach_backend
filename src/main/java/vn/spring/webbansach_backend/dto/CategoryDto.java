package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {
    private int categoryId;

    @NotBlank(message = "Tên thể loại không được bỏ trống!")
    private String categoryName;

}
