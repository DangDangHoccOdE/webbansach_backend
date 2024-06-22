package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.CategoryRepository;
import vn.spring.webbansach_backend.entity.Category;
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
}
