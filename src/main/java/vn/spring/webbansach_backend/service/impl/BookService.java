package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.Dto.BookDto;
import vn.spring.webbansach_backend.dao.BookRepository;
import vn.spring.webbansach_backend.dao.CategoryRepository;
import vn.spring.webbansach_backend.entity.Book;
import java.util.*;

import vn.spring.webbansach_backend.entity.Category;
import vn.spring.webbansach_backend.entity.Image;
import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.ICategoryService;

@Service
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ICategoryService iCategoryService;

    @Override
    @Transactional
    public void addBook(BookDto bookDto) {
        Book book = new Book();
        book.setBookName(bookDto.getBookName());
        book.setAuthor(bookDto.getAuthor());
        book.setDescription(bookDto.getDescription());
        book.setPrice(bookDto.getPrice());
        book.setISBN(bookDto.getIsbn());
        book.setQuantity(bookDto.getQuantity());
        book.setAverageRate(bookDto.getAverageRate());
        book.setListedPrice(bookDto.getListedPrice());
        book.setSoldQuantity(bookDto.getSoldQuantity());
        book.setDiscountPercent(bookDto.getDiscountPercent());

        List<Image> imageList = new ArrayList<>();
        if(bookDto.getRelatedImage()!=null){
            for(String imageString :bookDto.getRelatedImage()){
                Image image = new Image();
                image.setIcon(false);
                image.setImageData(imageString);
                image.setBook(book);
                imageList.add(image);
            }
        }

        if(bookDto.getThumbnail()!=null){
            Image image = new Image();
            image.setBook(book);
            image.setIcon(true);
            image.setImageData(bookDto.getThumbnail());
            imageList.add(image);
        }
        book.setImageList(imageList);

        List<Category> categoryList = new ArrayList<>();
        if(bookDto.getCategoryList()!=null){
            for(Integer categoryId:bookDto.getCategoryList()){
                Category category = iCategoryService.findCategoryByCategoryId((categoryId));
                categoryList.add(category);
            }
        }
        book.setCategoryList(categoryList);

        bookRepository.save(book);
    }
}
