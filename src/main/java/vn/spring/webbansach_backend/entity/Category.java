package vn.spring.webbansach_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private int categoryId;

    @Column(name = "categoryName",length = 256)
    private String categoryName;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "book_category",joinColumns = @JoinColumn(name = "categoryId"),inverseJoinColumns = @JoinColumn(name = "bookId"))
    private List<Book> bookList;


}
