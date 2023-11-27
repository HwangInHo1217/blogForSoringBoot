package com.ino.myblog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CategoryRequestDto {
    private String name;
    private Integer parentId; // null 허용
}
