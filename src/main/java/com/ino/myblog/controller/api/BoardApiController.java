package com.ino.myblog.controller.api;

import com.ino.myblog.config.auth.PrincipalDetail;
import com.ino.myblog.dto.BoardDto;
import com.ino.myblog.dto.CMRespDto;
import com.ino.myblog.dto.ReplySaveRequestDto;
import com.ino.myblog.dto.ResponseDto;
import com.ino.myblog.model.Board;
import com.ino.myblog.model.Reply;
import com.ino.myblog.service.BoardService;
import com.ino.myblog.service.CategoryService;
import com.ino.myblog.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController

public class BoardApiController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private  LikesService likesService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody BoardDto boardDto, @AuthenticationPrincipal PrincipalDetail principal){
        boardService.boardWrite(boardDto,principal.getUser());

        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.boardDelete(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board){
        boardService.boardUpdate(id, board);


        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    //데이터 받을 때 컨트롤러에서 DTO를 만들어서 받는게 좋다
    // DTO를 사용하지 않은 이유는!!
    // PROJECT 규모가 작아서
/*    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> replySave(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal){

        boardService.replyWrite(principal.getUser(), boardId, reply);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }*/
    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto){

        boardService.replyWrite(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable int replyId){
        boardService.replyDelete(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @PostMapping("/api/board/{boardId}/likes")
    public ResponseEntity<?> likes(@PathVariable int boardId, @AuthenticationPrincipal PrincipalDetail principalDetails){
        likesService.좋아요(boardId, principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요성공", null), HttpStatus.CREATED);
    }
    @DeleteMapping("/api/board/{boardId}/likes")
    public ResponseEntity<?> unLikes(@PathVariable int boardId, @AuthenticationPrincipal PrincipalDetail principalDetails){
        likesService.좋아요취소(boardId, principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요취소성공", null), HttpStatus.OK);
    }
}
