package com.ino.myblog.service;

import com.ino.myblog.handler.CustomException;
import com.ino.myblog.dto.SubscribeDto;
import com.ino.myblog.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId){
		
		// 쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, ");
		sb.append("(CASE WHEN EXISTS (SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id) THEN 1 ELSE 0 END) AS subscribeState, ");
		sb.append("(CASE WHEN ? = u.id THEN 1 ELSE 0 END) AS equalUserState, ");
		sb.append("u.profileImageUrl "); // profileImageUrl을 선택하는 코드를 추가합니다.
		sb.append("FROM user u INNER JOIN subscribe s ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?");
/*
		*/
		// 1.물음표 principalId
		// 2.물음표 principalId
		// 3.물음표 pageUserId
		
		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		// 쿼리 실행 (qlrm 라이브러리 필요 = DTO에 DB결과를 매핑하기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos =  result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}
	
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
