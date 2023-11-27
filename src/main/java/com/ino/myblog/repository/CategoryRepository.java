package com.ino.myblog.repository;

import com.ino.myblog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUserId(int userId);
    List<Category> findByParentCategoryId(Integer parentCategoryId);
    List<Category> findByParentCategory_Id (Integer parentId);

}
