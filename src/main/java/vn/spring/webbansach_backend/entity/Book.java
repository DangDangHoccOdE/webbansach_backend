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

    @Column(name="soldQuantity")
    private int soldQuantity;

    @Column(name="discountPercent")
    private float discountPercent;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_category",joinColumns = @JoinColumn(name = "bookId"),inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private List<Category> categoryList;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    private List<Image> imageList;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "book")
    private List<Review> reviewList;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "book")
    private List<OrderDetail> orderDetailList;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_wishlist",joinColumns = @JoinColumn(name = "bookId"),inverseJoinColumns = @JoinColumn(name = "wishListId"))
    private List<WishList> wishLists;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinTable(
            name = "cart_book",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "cartId")
    )
    private List<Cart> carts;

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
