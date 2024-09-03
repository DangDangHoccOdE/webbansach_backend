package vn.spring.webbansach_backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private int bookId;

    @Column(name = "bookName",nullable = false)
    private String bookName;

    @Column(name = "author",length = 512,nullable = false)
    private String author;

    @Column(name = "isbn")
    private String ISBN;

    @Column(name = "description",columnDefinition = "text")
    private String description ;

    @Column(name = "price",nullable = false)
    private double price;

    @Column(name = "listedPrice")
    private double listedPrice;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(name = "averageRate",nullable = false)
    private float averageRate;

    @Column(name="soldQuantity",nullable = false)
    private int soldQuantity;

    @Column(name="discountPercent")
    private float discountPercent;

    @Column(name = "publishingYear",nullable = false)
    private int publishingYear;

    @Column(name = "pageNumber",nullable = false)
    private int pageNumber;

    @Column(name = "language",nullable = false)
    private String language;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_category",joinColumns = @JoinColumn(name = "bookId"),inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private List<Category> categoryList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    private List<Image> imageList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    private List<Review> reviewList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "book")
    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_wishlist",joinColumns = @JoinColumn(name = "bookId"),inverseJoinColumns = @JoinColumn(name = "wishListId"))
    private List<WishList> wishLists;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    },mappedBy = "books")
    private List<CartItem> cartItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
