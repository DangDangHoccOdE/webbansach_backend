package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.CategoryRepository;
import vn.spring.webbansach_backend.entity.Category;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.service.inter.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category findCategoryByCategoryName(String name) {
        return categoryRepository.findByCategoryName(name);
    }

    @Override
    public Category findCategoryByCategoryId(int id) {
        return categoryRepository.findByCategoryId(id);
    }

    @Override
    public ResponseEntity<?> deleteCategory(int categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Thể loại không tồn tại!"));
        }
        categoryRepository.delete(category);
        return ResponseEntity.ok(new Notice("Đã xóa thành công"));
    }
}
