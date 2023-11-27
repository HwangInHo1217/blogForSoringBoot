package com.ino.myblog.service;



import com.ino.myblog.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {	
	private final LikesRepository likesRepository;


	@Transactional
	public void 좋아요(int boardId, int principalId) {

		likesRepository.mLikes(boardId, principalId);
	}
	
	@Transactional
	public void 좋아요취소(int boardId, int principalId) {
		likesRepository.mUnLikes(boardId, principalId);
	}
}
