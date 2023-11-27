package com.ino.myblog.controller.api;

import com.ino.myblog.dto.CategoryRequestDto;
import com.ino.myblog.dto.ResponseDto;
import com.ino.myblog.model.Category;
import com.ino.myblog.model.User;
import com.ino.myblog.repository.UserRepository;
import com.ino.myblog.service.CategoryService;
import com.ino.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController

public class CategoryApiController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/category")
    public ResponseDto<Category> createCategory(@RequestBody CategoryRequestDto categoryDto,
                                                Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return new ResponseDto<>(HttpStatus.OK.value(),
                categoryService.createCategory(categoryDto.getName(), categoryDto.getParentId(), user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseDto<Category> getCategoryById(@PathVariable int id) {
        return new ResponseDto<>(HttpStatus.OK.value(), categoryService.getCategoryById(id).orElse(null));
    }


    @PutMapping("/{id}")
    public ResponseDto<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
        return new ResponseDto<>(HttpStatus.OK.value(), categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/api/category/{categoryId}")
    public ResponseDto<Integer> deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
