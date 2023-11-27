package com.ino.myblog.service;

import com.ino.myblog.dto.BoardDto;
import com.ino.myblog.dto.ReplySaveRequestDto;
import com.ino.myblog.model.Board;

import com.ino.myblog.model.Category;
import com.ino.myblog.model.Reply;
import com.ino.myblog.model.User;
import com.ino.myblog.repository.BoardRepository;
import com.ino.myblog.repository.CategoryRepository;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void boardWrite(BoardDto boardDTO, User user) {
        int categoryId = boardDTO.getCategoryId();
        Category category = categoryRepository.findById(boardDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId+"  categortId : "+boardDTO.getCategoryId()));

        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setCategory(category);
        board.setUser(user);

        boardRepository.save(board);
    }

 /*   @Transactional
    public Page<Board> findBoardsByCategoryId(int categoryId, Pageable pageable) {
        return boardRepository.findByCategoryId(categoryId, pageable);
    }*/

    private void calculateLikesAndReplies(Board board) {
        int likeCount = board.getLikes().size(); // 좋아요 수 계산
        int replyCount = board.getReplys().size(); // 댓글 수 계산
        board.setLikeCount(likeCount);
        board.setReplyCount(replyCount);
    }

    public Page<Board> boardList(Pageable pageable){
        Page<Board> boards = boardRepository.findAll(pageable);
        boards.forEach(this::calculateLikesAndReplies);
        return boards;
    }

    @Transactional(readOnly = true)
    public Board boardDetail(int id) {
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        int likeCount = board.getLikes().size(); // 좋아요 수 계산
        board.setLikeCount(likeCount); // 좋아요 수 설정
        return board;
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
    public Page<Board> boardUserSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByUser_UsernameContaining(searchKeyword, pageable);
    }
}
/*서비스가 필요한 이유
*   트랜젝션 관리
* 서비스 의미*/