package vn.spring.webbansach_backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.Book;
import vn.spring.webbansach_backend.entity.Image;

@RepositoryRestResource(path = "images")
public interface ImageRepository extends JpaRepository<Image,Integer> {
    Page<Image> findByBook_BookId(@RequestParam("bookId") int bookId, Pageable pageable);
    Page<Image> findByBook_BookIdAndIsIcon(@RequestParam("bookId") int bookId,@RequestParam("isIcon") boolean isIcon,Pageable pageable);
    void deleteByImageId(int id);

}
