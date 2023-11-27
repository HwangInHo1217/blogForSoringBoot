package com.ino.myblog.repository;


import com.ino.myblog.model.Board;
import com.ino.myblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface BoardRepository extends JpaRepository<Board, Integer> {
    //Select * from user where username = 1?
  /*  Optional<User> findByUsername(String username);*/
   Page<Board>  findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByUserId(int userId, Pageable pageable);
    Page<Board> findByUser_UsernameContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByCategoryId(int categoryId, Pageable pageable);
    Page<Board> findByUserIdAndCategoryId(int userId, int categoryId, Pageable pageable);

 }
