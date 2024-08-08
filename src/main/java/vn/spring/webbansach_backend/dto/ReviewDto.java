package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private Float productRating;
    private Float shopRating;
    private Float deliveryRating;
    private Map<Integer,List<String>> mapImagesOfBook;
    private Map<Integer,String> mapContentsOfBook;
    private Map<Integer,String> mapVideoOfBook;
    private Map<Integer,Float> mapStarsOfBook;

    @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Ngày đánh giá không đúng định dạng: yyyy/MM/dd HH:mm:ss!")
    private String date;

}
