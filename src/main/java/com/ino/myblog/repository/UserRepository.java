package com.ino.myblog.repository;


import com.ino.myblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//DAO
//자동으로 bean으로 등록
//@Repository 생략가능
//@Autowired //의존성 주입
public interface UserRepository extends JpaRepository<User, Integer> {
    //Select * from user where username = 1?
    Optional<User> findByUsername(String username);
    User findByEmailHash(String emailHash);
}
//JPA Naming 쿼리
//select * from user where username = ? and password = ?;
//User findByUsernameAndPassword(String username, String password);


  /*  @Query(value = "select * from user where username = ? and password = ?",nativeQuery = true)
    User login(String username, String password);*/