package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "remark")
public class Remark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remarkId")
    private long remarkId;

    @Column(name = "rate")
    private float rate;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "bookId",nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "userId",nullable = false)
    private User user;

}
