package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0,message = "Giá phải lớn hơn 0")
    private Double price;

    @NotNull(message = "Giá niêm yết không được để trống")
    private Double listedPrice;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;

    @NotNull(message = "Số sao đánh giá không được để trống")
    private Float averageRate;

    @NotNull(message = "Số lượng đã bán không được thể bỏ trống")
    @Min(value = 0,message = "Số lượng phải lớn hơn 0")
    private Integer soldQuantity;

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    private Float discountPercent;

    @NotNull(message = "Số trang không được để trống")
    @Min(value = 1,message = "Số trang tối thiểu là 1")
    private Integer pageNumber;

    @NotNull(message = "Năm xuất bán không được bỏ trống")
    @Min(value = 0,message = "Năm xuất bản phải lớn hơn 0")
    @Max(value = 2024,message = "Năm xuất bán không được lón hơn năm hiện tại")
    private Integer publishingYear;

    @NotBlank(message = "Ngôn ngữ không được để trống")
    private String language;

    @NotBlank(message = "Ảnh chính không được để trống")
    private String thumbnail;

    @NotNull(message = "Danh sách ảnh liên quan không được để trống")
    private List<String> relatedImage;

    @NotNull(message = "Danh sách thể loại không được để trống")
    private List<Integer> categoryList;
}
