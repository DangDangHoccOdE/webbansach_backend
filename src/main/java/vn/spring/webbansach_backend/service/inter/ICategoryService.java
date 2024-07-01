package vn.spring.webbansach_backend.service.inter;

import vn.spring.webbansach_backend.entity.Category;

public interface ICategoryService {
    Category findCategoryByCategoryName(String name);
    Category findCategoryByCategoryId(int id);
    void deleteCategory(Category category);
}
