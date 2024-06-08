package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.Book;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book,Integer> {
}
