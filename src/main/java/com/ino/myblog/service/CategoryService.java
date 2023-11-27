package com.ino.myblog.service;

import com.ino.myblog.model.Category;
import com.ino.myblog.model.User;
import com.ino.myblog.repository.CategoryRepository;
import com.ino.myblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

/*    @Transactional
    public Category createCategory(String categoryName, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Category category = new Category();
        System.out.println("category = " + category);
        category.setName(categoryName);
        System.out.println("categoryName = " + categoryName);
        category.setUser(user);
        return categoryRepository.save(category);
    }*/
    @Transactional
    public Category createCategory(String categoryName, Integer parentId, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Category category = new Category();
        category.setName(categoryName);
        category.setUser(user);

        if (parentId != null) {
            Category parentCategory = getCategoryById(parentId)
                    .orElseThrow(() -> new NotFoundException("Parent category not found"));
            category.setParentCategory(parentCategory);
            parentCategory.getChildCategories().add(category); // 부모 카테고리의 자식 목록에 추가
        }

        return categoryRepository.save(category);
    }
    @Transactional
    public List<Category> findCategoriesByUser(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()) {
            return categoryRepository.findByUserId(user.get().getId());
        }
        return new ArrayList<>();
    }
    @Transactional(readOnly = true)

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

/*    @Transactional(readOnly = true)
    public List<Category> getAllCategoriesByUser(int userId) {
        return categoryRepository.findByUserId(userId);
    }*/

    @Transactional
    public Category updateCategory(int id, Category updatedCategory) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setParentCategory(updatedCategory.getParentCategory());
                    // 다른 필드 업데이트
                    return categoryRepository.save(category);
                }).orElseGet(() -> {
                    updatedCategory.setId(id);
                    return categoryRepository.save(updatedCategory);
                });
    }

    @Transactional
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
