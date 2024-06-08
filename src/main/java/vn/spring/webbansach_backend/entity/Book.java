package vn.spring.webbansach_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private int bookId;

    @Column(name = "bookName")
    private String bookName;

    @Column(name = "author",length = 512)
    private String author;

    @Column(name = "isbn")
    private String ISBN;

    @Column(name = "description",columnDefinition = "text")
    private String description ;

    @Column(name = "price")
    private double price;

    @Column(name = "listedPrice")
    private double listedPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "averageRate")
    private double averageRate;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_category",joinColumns = @JoinColumn(name = "bookId"),inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private List<Category> categoryList;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    private List<Image> imageList;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    private List<Remark> remarkList;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "book")
    private List<OrderDetail> orderDetailList;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "wishListId")
    private WishList wishList;


}
