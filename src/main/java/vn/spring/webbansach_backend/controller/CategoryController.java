package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.CategoryDto;
import vn.spring.webbansach_backend.service.inter.ICategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService iCategoryService;
    @Autowired
    public CategoryController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@Validated @RequestBody CategoryDto categoryDto){
        return iCategoryService.addCategory(categoryDto.getCategoryName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestBody CategoryDto categoryDto){
        return iCategoryService.editCategory(categoryDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable int categoryId){
        return iCategoryService.deleteCategory(categoryId);
    }

   @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{categoryIdNumber}/books/{bookIdNumber}")
    public ResponseEntity<?> deleteBookOfCategory(@PathVariable int categoryIdNumber,@PathVariable int bookIdNumber){
        return iCategoryService.deleteBookOfCategory(categoryIdNumber,bookIdNumber);
    }


}
