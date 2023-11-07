package com.ino.myblog.service;

import com.ino.myblog.dto.ReplySaveRequestDto;
import com.ino.myblog.model.Board;

import com.ino.myblog.model.Reply;
import com.ino.myblog.model.User;
import com.ino.myblog.repository.BoardRepository;
import com.ino.myblog.repository.ReplyRepository;
import com.ino.myblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public void boardWrite(Board board, User user){//title, content
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Board boardDetail(int id){

        return boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 상세보기 실패: 해당 글이 존재하지 않습니다.");
                });
    }
    @Transactional
    public void boardCount(int id){
        Board board=boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패: 해당 글이 존재하지 않습니다.");
                });//영속화 완료
        board.setCount(board.getCount()+1);
    }
    @Transactional
    public void boardDelete(int id){
        boardRepository.deleteById(id);
    }
    @Transactional
    public void boardUpdate(int id, Board requestBoard){
        Board board=boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패: 해당 글이 존재하지 않습니다.");
                });//영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        //해당함수 종료시에 트랜잭션이 service가 종료 될 때) 트랜잭션이 종료됨, 이때 더티체킹이 발생-자동업데이트(db flush)
    }

    @Transactional
    public void replyWrite(ReplySaveRequestDto replySaveRequestDto){
        Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
            return new IllegalArgumentException("댓글 쓰기 실패 : 사용자 id를 찾을 수 없습니다.");
        });//영속화 완료
        User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(()->{
            return new IllegalArgumentException("댓글 쓰기 실패 : 게시글 id를 찾을 수 없습니다.");
        });//영속화 완료
        Reply reply= Reply.builder()
                .user(user)
                .board(board)
                .content(replySaveRequestDto.getContent())
                .build();

        //reply.update(user,board, replySaveRequestDto.getContent());

        replyRepository.save(reply);
    }

    @Transactional
    public void replyDelete(int replyId) {
        replyRepository.deleteById(replyId);
    }
    @Transactional
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

}
/*서비스가 필요한 이유
*   트랜젝션 관리
* 서비스 의미*/