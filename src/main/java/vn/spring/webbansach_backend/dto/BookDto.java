package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookDto {
    private int bookId;

    @NotBlank(message = "Tên sách không được để trống")
    private String bookName;

    @NotBlank(message = "Tác giả không được để trống")
    private String author;

    @NotBlank(message = "ISBN không được để trống")
    private String isbn;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Giá không được để trống")
    private Double price;

    @NotNull(message = "Giá niêm yết không được để trống")
    private Double listedPrice;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;

    @NotNull(message = "Số sao đánh giá không được để trống")
    private Double averageRate;

    @NotNull(message = "Số lượng đã bán không được để trống")
    private Integer soldQuantity;

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    private Float discountPercent;

    @NotBlank(message = "Ảnh chính không được để trống")
    private String thumbnail;

    @NotNull(message = "Danh sách ảnh liên quan không được để trống")
    private List<String> relatedImage;

    @NotNull(message = "Danh sách thể loại không được để trống")
    private List<Integer> categoryList;
}
