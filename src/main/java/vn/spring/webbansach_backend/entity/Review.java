package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private long reviewId;

    @Column(name = "rate")
    private float rate;

    @Column(name = "content")
    private String content;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "bookId",nullable = false)
    private Book book;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @Column(name = "imageOne",columnDefinition = "LONGTEXT")
    @Lob
    private String imageOne;

    @Column(name = "imageTwo",columnDefinition = "LONGTEXT")
    @Lob
    private String imageTwo;

    @Column(name = "imageThree",columnDefinition = "LONGTEXT")
    @Lob
    private String imageThree;

    @Column(name = "imageFour",columnDefinition = "LONGTEXT")
    @Lob
    private String imageFour;

    @Column(name = "imageFive",columnDefinition = "LONGTEXT")
    @Lob
    private String imageFive;

    @Column(name = "video",columnDefinition = "LONGTEXT")
    @Lob
    private String video;

    @Column(name = "date",nullable = false)
    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "orderReviewId",nullable = false)
    private OrderReview orderReview;
}
