package vn.spring.webbansach_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDtoOfOrder {
    private Integer bookId;
    private String bookName;
    private double price;
    private double listedPrice;
    private float discountPercent;

}
