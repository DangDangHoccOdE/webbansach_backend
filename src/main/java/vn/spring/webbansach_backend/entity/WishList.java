package vn.spring.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wish_list")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishListId")
    private int wishListId;

    @Column(name = "wishListName",length = 256)
    private String wishListName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_wishlist",joinColumns = @JoinColumn(name = "wishListId"),inverseJoinColumns = @JoinColumn(name = "bookId"))
    private List<Book> bookList;

}
