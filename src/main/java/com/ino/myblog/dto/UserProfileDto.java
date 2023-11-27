package com.ino.myblog.dto;
import com.ino.myblog.model.Board;
import com.ino.myblog.model.Category;
import com.ino.myblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
    private boolean pageOwnerState;
    private int boardCount;
    private boolean subscribeState;
    private int subscribeCount;
    private User user;
    private List<Category> categories;
    private Page<Board> boards;

}
