package com.ino.myblog.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 100)
    private  String title;
    @Lob // 대용량데이터 처리
    private String content;//섬머노트 라이브러리 내용이 html 태그가 섞여 디자인되서 용량이 커지기 때문


    private int count;//조회수

    @ManyToOne(fetch = FetchType.EAGER) // 다대일 -> 다 = board 1 = user 즉 한명의 유저는 여러 게시글 작성 가능
    @JoinColumn(name="userId")
    @JsonIgnoreProperties({"boards"})
    private User user; // DB는 오브젝트를 저장 못함. 자바는 오브젝트 저장 가능

    @OneToMany(mappedBy = "board",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //mappedby 는 연관관계의 주인이 아니다(FK아님) db에 컬럼 만들지마 스프링아
    @JsonIgnoreProperties({"board"})
    @OrderBy("id desc")
    private List<Reply> replys;

    @CreationTimestamp
    private Timestamp createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // 실제 테이블에 생성될 외래키 컬럼명
    private Category category;

    @JsonIgnoreProperties({"board"})
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Likes> likes;
    @Transient // DB에 칼럼이 만들어지지 않는다.
    private boolean likeState;

    @Transient
    private int likeCount;

    @Transient
    private  int replyCount;
}
