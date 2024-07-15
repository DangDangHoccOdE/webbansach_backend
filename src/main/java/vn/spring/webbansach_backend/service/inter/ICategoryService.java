package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import vn.spring.webbansach_backend.dto.CategoryDto;
import vn.spring.webbansach_backend.entity.Category;

public interface ICategoryService {
    void saveCategory(Category category);
    Category findCategoryByCategoryName(String name);
    Category findCategoryByCategoryId(int id);
    ResponseEntity<?> deleteCategory(int categoryId);
    ResponseEntity<?> addCategory(String newCategoryName);
    ResponseEntity<?> editCategory(CategoryDto categoryDto);
    ResponseEntity<?> deleteBookOfCategory(int categoryId,int bookId);
}
