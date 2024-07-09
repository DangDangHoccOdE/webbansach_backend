package vn.spring.webbansach_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonBackReference
    private User user;

    @Column(name = "quantity",nullable = false,columnDefinition = "int default 0")
    private int quantity=0;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_wishlist",joinColumns = @JoinColumn(name = "wishListId"),inverseJoinColumns = @JoinColumn(name = "bookId"))
    @JsonIgnore
    private List<Book> bookList;

}
