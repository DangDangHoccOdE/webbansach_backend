package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.CategoryRepository;
import vn.spring.webbansach_backend.dto.CategoryDto;
import vn.spring.webbansach_backend.entity.Book;
import vn.spring.webbansach_backend.entity.Category;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.ICategoryService;
import java.util.*;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final IBookService iBookService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,@Lazy IBookService iBookService) {
        this.categoryRepository = categoryRepository;
        this.iBookService = iBookService;
    }

    @Override
    public Category findCategoryByCategoryName(String name) {
        return categoryRepository.findByCategoryName(name);
    }

    @Override
    public Category findCategoryByCategoryId(int id) {
        return categoryRepository.findByCategoryId(id);
    }

    @Override
    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addCategory(String newCategoryName) {
        Category category = categoryRepository.findByCategoryName((newCategoryName));
        if(category!=null){
            return ResponseEntity.badRequest().body(new Notice("Tên thể loại đã tồn tại!"));
        }
        Category newCategory= new Category();
        newCategory.setCategoryName(newCategoryName);
        categoryRepository.save(newCategory);
        return ResponseEntity.ok(new Notice("Đã thêm thể loại mới thành công!"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteCategory(int categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Thể loại không tồn tại!"));
        }
        categoryRepository.delete(category);
        return ResponseEntity.ok(new Notice("Đã xóa thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> editCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findByCategoryId(categoryDto.getCategoryId());
        if(category == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Thể loại không tồn tại!"));
        }
        String newCategoryName = categoryDto.getCategoryName();
        if(newCategoryName.equals(category.getCategoryName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notice("Tên thể loại mới không thể trùng tên thể loại cũ!"));
        }
        if(categoryRepository.existsByCategoryName(newCategoryName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notice("Tên thể loại mới đã tồn tại!"));
        }
        category.setCategoryName(newCategoryName);
        categoryRepository.save(category);
        return ResponseEntity.ok(new Notice("Chỉnh sửa tên thể loại thành công!"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteBookOfCategory(int categoryId,int bookId) {
        Book book = iBookService.findBookById(bookId);
        Category category = findCategoryByCategoryId(categoryId);

        if(book == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sách cần xóa"));
        }
        if(category == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thể loại mốn xóa sách!"));
        }
        List<Category> categoryList = book.getCategoryList();
        List<Book> bookList = category.getBookList();

        if(!bookList.contains(book)){
            return ResponseEntity.badRequest().body(new Notice("Thể loại không chứa sách cần xóa!"));
        }

        List<Category> categoryUpdate = new ArrayList<>();
        for(Category category1 : categoryList){
            if(category1!=category){
                categoryUpdate.add(category1);
            }
        }
        category.setBookQuantity(category.getBookQuantity()-1);
        categoryRepository.save(category);

        book.setCategoryList(categoryUpdate);
        return ResponseEntity.ok(new Notice("Đã xóa sách khỏi thể loại thành công!"));
    }
}
