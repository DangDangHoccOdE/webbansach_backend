package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageId")
    private int imageId;

    @Column(name = "isIcon")
    private boolean isIcon;

    @Column(name = "imageData",columnDefinition = "LONGTEXT")
    @Lob
    private String imageData;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "bookId",nullable = false)
    private Book book;
}
