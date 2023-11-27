package com.ino.myblog.service;

import com.ino.myblog.dto.UserProfileDto;
import com.ino.myblog.handler.CustomException;
import com.ino.myblog.model.*;
import com.ino.myblog.repository.BoardRepository;
import com.ino.myblog.repository.CategoryRepository;
import com.ino.myblog.repository.SubscribeRepository;
import com.ino.myblog.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRespository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JavaMailSender emailSender;
    private final SubscribeRepository subscribeRepository;

    @Value("${file.path}")
    private String uploadFolder;
    @Transactional(readOnly = true)
    public User findUser(String username){
        User user =userRespository.findByUsername(username).orElseGet(()->
        {
            return new User();
        });
        return user;
    }
    @Transactional//select 시 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료( 정합성 )
    public void save(User user){
        String rawPassword=user.getPassword();
        String encPassword= encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        //user.setProfileImageUrl("");
        if(user.getEmail()!="of_kakao")
            user.setEmailCheck(EmailCheck.NO);
        userRespository.save(user);
        Category defaultCategory = new Category();
        defaultCategory.setName("기본 카테고리");
        defaultCategory.setUser(user); // 새로 생성된 사용자에 할당
        categoryRepository.save(defaultCategory);
    }
    @Transactional
    public void userUpdate(User user){
        //수정시에는 영속성 컨텍스트 User 오브젝트를 영속화 시키고, 영속화 된 User 오브젝트를 수정하면됨
        //select를 해서 user오브젝트를 db로 부터 가져온는 이유는 영속화를 하기 위해서
        //영속화 된 오브젝트를 변경하면 자동으로 db에 update문을 날려줌
        User persistance = userRespository.findById(user.getId()).orElseThrow(()->{
            return new IllegalArgumentException("회원찾기 실패");
        });
        //Validate 체크 post 공격 방지
        if(persistance.getOauth()==null || persistance.getOauth().equals("")){
            if(user.getPassword()!=null && !user.getPassword().isEmpty()) {
                String rawPassword = user.getPassword();
                String encPaswword = encoder.encode(rawPassword);
                persistance.setPassword(encPaswword);
            }
            persistance.setEmail(user.getEmail());
            persistance.setBio(user.getBio());
        }
        //회원 수정 함수 종료=서비스종료 = 트랜잭션 종료 = commit됨
    }
    public void sendVerificationEmail(String to, String token) {
        String verificationLink = "http://localhost:8000/auth/verify?token=" + token;
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>이메일 인증</h3>"
                + "<p>아래의 링크를 클릭하여 이메일 인증을 완료해주세요:</p>"
                + "<a href='" + verificationLink + "'>이메일 인증하기</a>";
        try {
            helper.setText(htmlMsg, true); // true를 설정하여 HTML 형식으로 전송
            helper.setTo(to);
            helper.setSubject("졸업작품 테스트) 블로그 이메일 인증 메일입니다.");
            helper.setFrom("hih1217@skuniv.ac.kr");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(mimeMessage);
    }
    @Transactional
    public boolean verifyUser(String emailHash) {
        // 데이터베이스에서 이메일 해시로 사용자를 찾습니다.
        User user = userRespository.findByEmailHash(emailHash);

        if (user != null) {
            // 사용자를 찾았으면 인증 상태를 업데이트합니다.
            user.setEmailCheck(EmailCheck.YES);
            userRespository.save(user);
            return true;
        } else {
            // 사용자를 찾지 못했으면 false를 반환합니다.
            user.setEmailCheck(EmailCheck.NO);
            return false;
        }
    }
    @Transactional(readOnly = true)
    public UserProfileDto userProfile(int pageUserId, int principalId) {
        UserProfileDto dto = new UserProfileDto();

        // SELECT * FROM image WHERE userId = :userId;
        User userEntity = userRespository.findById(pageUserId).orElseThrow(()-> {
            // throw -> return 으로 변경
            return new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });

        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId == principalId);
        dto.setBoardCount(userEntity.getBoards().size());
        int subscribeState =  subscribeRepository.mSubscribeState(principalId, pageUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);

        dto.setSubscribeState(subscribeState == 1);
        dto.setSubscribeCount(subscribeCount);

        // 좋아요 카운트 추가하기
        userEntity.getBoards().forEach((board)->{
            board.setLikeCount(board.getLikes().size());
            board.setReplyCount(board.getReplys().size());
        });


        return dto;
    }
    @Transactional(readOnly = true)
    public Page<Board> userBoardList(int id, Pageable pageable){
        return boardRepository.findByUserId(id,pageable);
     }
    @Transactional(readOnly = true)
    public List<Category> userCategoryList(int id){
        return categoryRepository.findByUserId(id);
    }
    @Transactional(readOnly = true)
    public Page<Board> boardUserSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByUser_UsernameContaining(searchKeyword, pageable);
    }
    @Transactional(readOnly = true)
    public Page<Board> userBoardListByCategory(int userId, int categoryId, Pageable pageable) {
        return boardRepository.findByUserIdAndCategoryId(userId, categoryId, pageable);
    }
    @Transactional
    public User userProfileImageUpdate(int principalId, MultipartFile profileImageFile) {
        UUID uuid = UUID.randomUUID(); // uuid
        String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename(); // 1.jpg
        System.out.println("이미지 파일이름 : "+imageFileName);

        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        // 통신, I/O -> 예외가 발생할 수 있다.
        try {
            Files.write(imageFilePath, profileImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        User userEntity = userRespository.findById(principalId).orElseThrow(()->{
            // throw -> return 으로 변경
            return new CustomException("유저를 찾을 수 없습니다.");
        });
        userEntity.setProfileImageUrl(imageFileName);

        return userEntity;
    } // 더티체킹으로 업데이트 됨.

}
/*서비스가 필요한 이유
*   트랜젝션 관리
* 서비스 의미*/