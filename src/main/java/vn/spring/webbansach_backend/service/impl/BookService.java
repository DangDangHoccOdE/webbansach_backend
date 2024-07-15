package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.dao.BookRepository;
import vn.spring.webbansach_backend.entity.*;

import java.util.*;

import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.ICategoryService;
import vn.spring.webbansach_backend.service.inter.IImageService;
import vn.spring.webbansach_backend.service.inter.IWishListService;

@Service
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final ICategoryService iCategoryService;

    private final IWishListService iWishListService;

    private final IImageService iImageService;
    @Autowired
    public BookService(BookRepository bookRepository,@Lazy ICategoryService iCategoryService, @Lazy IWishListService iWishListService, IImageService iImageService) {
        this.bookRepository = bookRepository;
        this.iCategoryService = iCategoryService;
        this.iWishListService = iWishListService;
        this.iImageService = iImageService;
    }

    @Override
    @Transactional
    public void addBook(BookDto bookDto) {
        Book book = new Book();
        updateAndSaveBook(bookDto,book);
    }

    @Override
    public Book findBookById(int bookId) {
        return bookRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public ResponseEntity<?> editBook(BookDto bookDto) {
        Book book = bookRepository.findByBookId(bookDto.getBookId());
        if(book==null){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Sách không tồn tại!"));
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
            category.setBookQuantity(category.getBookQuantity()+1);
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
    @Transactional
    public ResponseEntity<?> deleteBook(int bookId) {
        Book book = bookRepository.findByBookId(bookId);
        if(book == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Sách không tồn tại!"));
        }

        try{
            List<WishList> wishLists = book.getWishLists();
            for(WishList wishList : wishLists){
                wishList.setQuantity(wishList.getQuantity()-1);
                iWishListService.saveWishList(wishList);
            }

            List<Category> categoryList = book.getCategoryList();
            for(Category category : categoryList){
                category.setBookQuantity(category.getBookQuantity()-1);
                iCategoryService.saveCategory(category);
            }
            bookRepository.delete(book);
            return ResponseEntity.ok(new Notice("Đã xóa sách thành công!"));
        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage());
            return ResponseEntity.badRequest().body(new Notice("Không thể xóa sách!"+e.getMessage()));
        }
    }
}
