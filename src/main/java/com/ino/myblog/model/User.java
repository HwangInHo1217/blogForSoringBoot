package com.ino.myblog.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.ino.myblog.model.Board;
import java.sql.Timestamp;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//빌더패턴
@Entity//user클래스카 mysql에서 자동 테이블 생성
//@DynamicInsert // insert 시 null 값 제외하고 인설트
public class User {
    @Id//프라이머리키
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql 시퀀스 사용
    private int id;//시퀀스,auto_increment


    @Column(nullable=false,length=100, unique = true)
    private String username;

    @Column(length=100) //암호화된 비밀번호 넣을거임
    private String password;

    @Column(nullable=false,length=50)
    private String email;

    @Column(length = 100)
    private String emailHash;

    @Enumerated(EnumType.STRING)
    private EmailCheck emailCheck;

    //@ColumnDefault("'user'")
    //enum 도메인 정의 할 때 사용
    @Enumerated(EnumType.STRING)//
    private RoleType role;//Enum을 쓰는게 좋음. 데이터에 도메인 생성가능 //admin, user, manger 등 권한

    //private TimeStamp updateDate;
    @CreationTimestamp // 시간 자동 입력
    private Timestamp createDate;

    private String oauth;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user"})
    private List<Board> boards;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user"})
    private List<Category> categorys;

    private String bio; // 자기 소개

    private String profileImageUrl; // 사진
}