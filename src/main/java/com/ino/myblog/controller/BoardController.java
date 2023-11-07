package com.ino.myblog.controller;

import com.ino.myblog.config.auth.PrincipalDetail;

import com.ino.myblog.repository.BoardRepository;
import com.ino.myblog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping({"","/"})
    public String index(Model model,
                        @PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC)Pageable pageable,
                        String searchKeyword){
        if(searchKeyword == null){
            model.addAttribute("boards",boardService.boardList(pageable));
        }else{
            model.addAttribute("boards",boardService.boardSearchList(searchKeyword,pageable));
        }

        return "index"; //view resolver 작동
     }
     @GetMapping("/board/{id}")
     public String findById(@PathVariable int id, Model model){
        model.addAttribute(boardService.boardDetail(id));
        boardService.boardCount(id);
        return "board/detail";

     }

    @GetMapping("board/saveForm")
    public String saveForm(){//@AuthenticationPrincipal PrincipalDetail principal//세션접근
        //System.out.println("principal = " + principal.getUsername());
        return "board/saveForm";
    }
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model){
        model.addAttribute("board",boardService.boardDetail(id));
        return "board/updateForm";
    }

}
