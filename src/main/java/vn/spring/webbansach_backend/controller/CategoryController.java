package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dao.CategoryRepository;
import vn.spring.webbansach_backend.dto.CategoryDto;
import vn.spring.webbansach_backend.service.inter.ICategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ICategoryService iCategoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@Validated @RequestBody CategoryDto categoryDto){
        return iCategoryService.addCategory(categoryDto.getCategoryName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable int categoryId){
        return iCategoryService.deleteCategory(categoryId);
    }
}
