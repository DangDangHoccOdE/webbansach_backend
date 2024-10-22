package vn.spring.webbansach_backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.Book;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book,Integer> {
    Book findByBookId(int id);
    Page<Book> findByBookNameContaining(@RequestParam("bookName") String bookName, Pageable pageable);

    Page<Book> findByCategoryList_categoryId(@RequestParam("categoryId") int categoryId, Pageable pageable);

    Page<Book> findByBookNameContainingAndCategoryList_categoryId(@RequestParam("bookName") String bookName,@RequestParam("categoryId") int categoryId, Pageable pageable);

    @Query("SELECT b from Book b JOIN b.wishLists w where w.wishListId =:wishListId")
    Page<Book> findBooksInWishList(@Param("wishListId") Integer wishListId, Pageable pageable);
    int countBy();
}