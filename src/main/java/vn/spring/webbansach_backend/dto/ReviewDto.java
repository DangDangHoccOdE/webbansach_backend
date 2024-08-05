package vn.spring.webbansach_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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


}
