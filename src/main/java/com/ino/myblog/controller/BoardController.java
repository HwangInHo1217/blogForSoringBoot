package com.ino.myblog.controller;

import com.ino.myblog.config.auth.PrincipalDetail;

import com.ino.myblog.model.Category;
import com.ino.myblog.repository.BoardRepository;
import com.ino.myblog.service.BoardService;
import com.ino.myblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"", "/"})
    public String index(Model model,
                        @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        String searchKeyword, String filterType) {
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            model.addAttribute("boards", boardService.boardList(pageable));
        } else if ("user".equals(filterType)) {
            // 사용자 이름으로 검색
            model.addAttribute("boards", boardService.boardUserSearchList(searchKeyword, pageable));
        } else {
            // 기본은 제목으로 검색
            model.addAttribute("boards", boardService.boardSearchList(searchKeyword, pageable));
        }

        return "index"; // view resolver 작동
        }
    @ModelAttribute("categories")
    public List<Category> categories(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            // 여기서 CategoryService를 통해 사용자 이름에 해당하는 카테고리 목록을 가져옵니다.
            return categoryService.findCategoriesByUser(username);
        }
        return new ArrayList<>(); // 비로그인 상태일 경우 빈 리스트 반환
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
