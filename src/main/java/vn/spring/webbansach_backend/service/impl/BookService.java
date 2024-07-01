package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.dao.BookRepository;
import vn.spring.webbansach_backend.entity.Book;
import java.util.*;

import vn.spring.webbansach_backend.entity.Category;
import vn.spring.webbansach_backend.entity.Image;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.ICategoryService;
import vn.spring.webbansach_backend.service.inter.IImageService;

@Service
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IImageService iImageService;

    @Override
    @Transactional
    public void addBook(BookDto bookDto) {
        Book book = new Book();
        updateAndSaveBook(bookDto,book);
    }

    @Override
    @Transactional
    public ResponseEntity<?> editBook(BookDto bookDto) {
        Book book = bookRepository.findByBookId(bookDto.getBookId());
        if(book==null){
          return   ResponseEntity.badRequest().body(new Notice("Sách không tồn tại!"));
        }else {

            // XÓa ảnh
            for (Image i : book.getImageList()) {
                iImageService.deleteImage(i);
            }

            updateAndSaveBook(bookDto, book);
            return ResponseEntity.ok().body(new Notice("Chỉnh sửa sách thành công!"));
        }
    }

    private void updateAndSaveBook(BookDto bookDto, Book book) {
        // Các thiết lập thông tin sách...

        List<Image> imageList = new ArrayList<>();
        if (bookDto.getRelatedImage() != null) {
            for (String imageString : bookDto.getRelatedImage()) {
                Image image = new Image();
                image.setIcon(false);
                image.setImageData(imageString);
                image.setBook(book);
                imageList.add(image);
            }
        }

        // Thêm thumbnail vào cuối danh sách imageList
        if (bookDto.getThumbnail() != null) {
            Image thumbnailImage = new Image();
            thumbnailImage.setBook(book);
            thumbnailImage.setIcon(true);
            thumbnailImage.setImageData(bookDto.getThumbnail());
            imageList.add(thumbnailImage);
        }


        book.setImageList(imageList);

        // Lưu book vào cơ sở dữ liệu
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }


}
