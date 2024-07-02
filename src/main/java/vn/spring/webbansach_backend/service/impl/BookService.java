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
          return ResponseEntity.badRequest().body(new Notice("Sách không tồn tại!"));
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
        book.setBookName(bookDto.getBookName());
        book.setPrice(bookDto.getPrice());
        book.setISBN(bookDto.getIsbn());
        book.setDiscountPercent(bookDto.getDiscountPercent());
        book.setQuantity(bookDto.getQuantity());
        book.setSoldQuantity(bookDto.getSoldQuantity());
        book.setListedPrice(bookDto.getListedPrice());
        book.setAuthor(bookDto.getAuthor());
        book.setAverageRate(bookDto.getAverageRate());
        book.setDescription(bookDto.getDescription());

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

        if (bookDto.getThumbnail() != null) {
            Image thumbnailImage = new Image();
            thumbnailImage.setBook(book);
            thumbnailImage.setIcon(true);
            thumbnailImage.setImageData(bookDto.getThumbnail());
            imageList.add(thumbnailImage);
        }


        book.setImageList(imageList);

        // lưu category
        List<Category> categoryList = new ArrayList<>();
        for(int id : bookDto.getCategoryList()){
            Category category = iCategoryService.findCategoryByCategoryId(id);
            categoryList.add(category);
        }

        book.setCategoryList(categoryList);

        // Lưu book vào cơ sở dữ liệu
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteBook(int bookId) {
        Book book = bookRepository.findByBookId(bookId);
        if(book == null){
            return ResponseEntity.badRequest().body(new Notice("Sách không tồn tại!"));
        }

        try{
            bookRepository.delete(book);
            return ResponseEntity.ok(new Notice("Đã xóa sách thành công!"));
        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage());
            return ResponseEntity.badRequest().body(new Notice("Không thể xóa sách!"+e.getMessage()));
        }
    }
}
