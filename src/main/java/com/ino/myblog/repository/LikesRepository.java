package com.ino.myblog.repository;

import com.ino.myblog.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

	@Modifying
	@Query(value = "INSERT INTO likes(boardId, userId, createDate) VALUES(:boardId, :principalId, now())", nativeQuery = true)
	int mLikes(@Param("boardId") int imageId, @Param("principalId") int principalId);

	@Modifying
	@Query(value = "DELETE FROM likes WHERE boardId = :boardId AND userId = :principalId", nativeQuery = true)
	int mUnLikes(@Param("boardId") int boardId, @Param("principalId") int principalId);
}
